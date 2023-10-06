package com.nayaragaspar.lancamentos.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nayaragaspar.lancamentos.model.dto.SalvarClienteDto;
import com.nayaragaspar.lancamentos.model.dto.SalvarContaDto;
import com.nayaragaspar.lancamentos.model.dto.TransacaoDto;
import com.nayaragaspar.lancamentos.model.entity.Cliente;
import com.nayaragaspar.lancamentos.model.entity.Conta;
import com.nayaragaspar.lancamentos.model.enums.TipoConta;
import com.nayaragaspar.lancamentos.model.enums.TipoTransacao;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ContaControllerTest {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static Conta conta;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setPort(8888)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

    }

    @Test
    @Order(1)
    @DisplayName("Teste salvar cliente / conta")
    void testSalvarConta() throws JsonMappingException, JsonProcessingException {
        SalvarClienteDto clienteDto = new SalvarClienteDto("Cliente Um", "12345678910", "um@gmail.com", "");
        var contentCliente = given().spec(specification)
                .basePath("/cliente")
                .contentType("application/json")
                .body(clienteDto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Cliente cliente = objectMapper.readValue(contentCliente, Cliente.class);

        SalvarContaDto contaDto = new SalvarContaDto(cliente.getId(), TipoConta.CORRENTE, new BigDecimal("2000"));

        var content = given().spec(specification)
                .basePath("/conta")
                .contentType("application/json")
                .body(contaDto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        conta = objectMapper.readValue(content, Conta.class);

        assertNotNull(conta);
        assertNotNull(conta.getId());
        assertEquals("12345678910", conta.getCliente().getDocumento());
    }

    @Test
    @Order(2)
    @DisplayName("Teste transação única em conta")
    void testTransacao() throws JsonMappingException, JsonProcessingException {
        TransacaoDto transacaoDto = new TransacaoDto(TipoTransacao.DEPOSITO, new BigDecimal("500"));
        List<TransacaoDto> list = new ArrayList<>();
        list.add(transacaoDto);

        String retorno = given().spec(specification)
                .basePath("/conta/{id}/transacao")
                .pathParam("id", conta.getId())
                .contentType("application/json")
                .body(list)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertEquals("Transações realizadas com sucesso!", retorno);
    }

    @Test
    @Order(3)
    @DisplayName("Teste transação concorrente em conta")
    void testTransacaoConcorrente() throws JsonMappingException, JsonProcessingException, InterruptedException {
        Thread thread = new Thread(() -> {
            TransacaoDto transacaoDto = new TransacaoDto(TipoTransacao.DEPOSITO, new BigDecimal("100"));
            List<TransacaoDto> list = new ArrayList<>();
            list.add(transacaoDto);

            String retorno1 = given().spec(specification)
                    .basePath("/conta/{id}/transacao")
                    .pathParam("id", conta.getId())
                    .contentType("application/json")
                    .body(list)
                    .when()
                    .post()
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .asString();

            assertEquals("Transações realizadas com sucesso!", retorno1);
        });
        thread.setName("Thread 1");
        thread.start();

        Thread.sleep(1000L);
        Thread.currentThread().setName("Thread 2");
        /* --------------------------- */

        TransacaoDto transacaoDto = new TransacaoDto(TipoTransacao.SAQUE, new BigDecimal("1000"));
        List<TransacaoDto> list = new ArrayList<>();
        list.add(transacaoDto);

        String retorno2 = given().spec(specification)
                .basePath("/conta/{id}/transacao")
                .pathParam("id", conta.getId())
                .contentType("application/json")
                .body(list)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertEquals("Transações realizadas com sucesso!", retorno2);
    }

    @Test
    @Order(4)
    @DisplayName("Teste saldo conta após transação concorrente")
    void testSaldoConta() throws JsonMappingException, JsonProcessingException {
        String retorno = given().spec(specification)
                .basePath("/conta/{id}/saldo")
                .pathParam("id", conta.getId())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        System.out.println("Saldo: " + retorno);

        assertEquals(new BigDecimal("1600.00"), new BigDecimal(retorno));
    }

    @Test
    @Order(5)
    @DisplayName("Teste de transação com conta inválida")
    void testContaInválida() throws JsonMappingException, JsonProcessingException {
        TransacaoDto transacaoDto = new TransacaoDto(TipoTransacao.SAQUE, new BigDecimal("1000"));
        List<TransacaoDto> list = new ArrayList<>();
        list.add(transacaoDto);

        String retorno = given().spec(specification)
                .basePath("/conta/{id}/transacao")
                .pathParam("id", "3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .contentType("application/json")
                .body(list)
                .when()
                .post()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .asString();

        assertTrue(retorno.contains("Conta não encontrado"));
    }
}
