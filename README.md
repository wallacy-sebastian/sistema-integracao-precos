# Sistema de integração de preços

Alunos: João Vitor Ferreira e Wallacy Sebastian Aparecido Jeronimo de Almeida
* * *
Este sistema terá como objetivo capturar informações de produtos de diferentes sites de e-commerce, com o objetivo de compará-los em relação a preço e avaliações dos usuários.

## Sites e seções

A princípio, para este projeto, este sistema será capaz de capturar dados dos sites **Kabum**, **Lojas Colombo** e **Londritech**, com foco em duas seções: *Mouse* e *Monitor*.

## Informações a serem coletadas

Os dados a serem coletados serão em relação ao produto, como o nome, a descrição do produto, a URL da imagem, o valor e a seção à qual este pertence. Algumas outras informações relevantes sobre o produto são: as especificações, a avaliação, o tipo de pagamento, e quem entrega.

Sobre as especificações, pode-se obter em comum a marca e o modelo (para auxiliar na identificação do produto), e o restante da ficha técnica será coletado para ser exibido de forma descritiva, como a descrição.

A avaliação do produto é uma informação importante, pois servirá para comparar em qual e-commerce este produto possuirá mais ou menos problemas. Servirá como comparativo, junto com o preço. Os dados de avaliação coletados serão: o nome, o comentário (se houver), a data em que foi feita a avaliação e qual a nota da avaliação.

Por fim, o pagamento de um produto pode ser feito de várias formas, como cartão de crédito, cartão de débito, boleto, etc. Alguns tipos de pagamento possuem a opção de parcelamento, e alguns sites oferecem desconto de acordo com a quantidade de parcelas ou se o pagamento é feito à vista. Portanto, os dados coletados em relação ao pagamento será o tipo de pagamento, a quantidade de parcelas (se o tipo de pagamento oferecer isso) e o desconto ofertado de acordo com a quantidade de parcelas.

## Diagrama ER

![diagrama_er.png](docs/diagrama_er.png)

[Link editável do diagrama](https://drive.google.com/file/d/1aGtF2vE7muIdesT2dGh71oujb8WyNCdS/view?usp=sharing)

## Implementação SQL

```sql

--
-- PostgreSQL database dump
--

-- Dumped from database version 12.8 (Ubuntu 12.8-0ubuntu0.20.04.1)
-- Dumped by pg_dump version 12.10 (Ubuntu 12.10-1.pgdg20.04+1)

-- Started on 2022-02-15 00:29:53 -03

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 18317)
-- Name: integracao_precos; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA integracao_precos;


ALTER SCHEMA integracao_precos OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 210 (class 1259 OID 18425)
-- Name: avaliacao; Type: TABLE; Schema: integracao_precos; Owner: postgres
--

CREATE TABLE integracao_precos.avaliacao (
    id_produto integer,
    nome character varying(50),
    comentario character varying(500),
    data date,
    qtd_estrelas integer,
    id integer NOT NULL
);


ALTER TABLE integracao_precos.avaliacao OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 18423)
-- Name: avaliacao_id_seq; Type: SEQUENCE; Schema: integracao_precos; Owner: postgres
--

CREATE SEQUENCE integracao_precos.avaliacao_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE integracao_precos.avaliacao_id_seq OWNER TO postgres;

--
-- TOC entry 3017 (class 0 OID 0)
-- Dependencies: 209
-- Name: avaliacao_id_seq; Type: SEQUENCE OWNED BY; Schema: integracao_precos; Owner: postgres
--

ALTER SEQUENCE integracao_precos.avaliacao_id_seq OWNED BY integracao_precos.avaliacao.id;


--
-- TOC entry 212 (class 1259 OID 18453)
-- Name: entrega; Type: TABLE; Schema: integracao_precos; Owner: postgres
--

CREATE TABLE integracao_precos.entrega (
    id_produto integer NOT NULL,
    nome_transportadora character varying(50),
    valor double precision,
    id integer NOT NULL
);


ALTER TABLE integracao_precos.entrega OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 18451)
-- Name: entrega_id_seq; Type: SEQUENCE; Schema: integracao_precos; Owner: postgres
--

CREATE SEQUENCE integracao_precos.entrega_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE integracao_precos.entrega_id_seq OWNER TO postgres;

--
-- TOC entry 3018 (class 0 OID 0)
-- Dependencies: 211
-- Name: entrega_id_seq; Type: SEQUENCE OWNED BY; Schema: integracao_precos; Owner: postgres
--

ALTER SEQUENCE integracao_precos.entrega_id_seq OWNED BY integracao_precos.entrega.id;


--
-- TOC entry 208 (class 1259 OID 18371)
-- Name: pagamento; Type: TABLE; Schema: integracao_precos; Owner: postgres
--

CREATE TABLE integracao_precos.pagamento (
    id integer NOT NULL,
    tipo integer,
    vezes integer,
    valor double precision,
    id_produto integer
);


ALTER TABLE integracao_precos.pagamento OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 18369)
-- Name: pagamentos_id_seq; Type: SEQUENCE; Schema: integracao_precos; Owner: postgres
--

CREATE SEQUENCE integracao_precos.pagamentos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE integracao_precos.pagamentos_id_seq OWNER TO postgres;

--
-- TOC entry 3019 (class 0 OID 0)
-- Dependencies: 207
-- Name: pagamentos_id_seq; Type: SEQUENCE OWNED BY; Schema: integracao_precos; Owner: postgres
--

ALTER SEQUENCE integracao_precos.pagamentos_id_seq OWNED BY integracao_precos.pagamento.id;


--
-- TOC entry 204 (class 1259 OID 18320)
-- Name: produto; Type: TABLE; Schema: integracao_precos; Owner: postgres
--

CREATE TABLE integracao_precos.produto (
    id integer NOT NULL,
    nome character varying(500),
    url_imagem character varying(500),
    descricao character varying(500),
    modelo character varying(50),
    marca character varying(50),
    ficha_tecnica character varying(500),
    valor double precision,
    created_at date,
    secao integer,
    integracao_id integer,
    loja integer
);


ALTER TABLE integracao_precos.produto OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 18318)
-- Name: produto_id_seq; Type: SEQUENCE; Schema: integracao_precos; Owner: postgres
--

CREATE SEQUENCE integracao_precos.produto_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE integracao_precos.produto_id_seq OWNER TO postgres;

--
-- TOC entry 3020 (class 0 OID 0)
-- Dependencies: 203
-- Name: produto_id_seq; Type: SEQUENCE OWNED BY; Schema: integracao_precos; Owner: postgres
--

ALTER SEQUENCE integracao_precos.produto_id_seq OWNED BY integracao_precos.produto.id;


--
-- TOC entry 213 (class 1259 OID 18470)
-- Name: produto_integracao; Type: TABLE; Schema: integracao_precos; Owner: postgres
--

CREATE TABLE integracao_precos.produto_integracao (
    id_integracao integer NOT NULL,
    id_produto integer NOT NULL
);


ALTER TABLE integracao_precos.produto_integracao OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 18363)
-- Name: usuario; Type: TABLE; Schema: integracao_precos; Owner: postgres
--

CREATE TABLE integracao_precos.usuario (
    id integer NOT NULL,
    nome character varying(50),
    login character varying(20),
    senha character varying(32),
    cep character varying(10),
    nascimento date,
    avatar character varying(100)
);


ALTER TABLE integracao_precos.usuario OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 18361)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: integracao_precos; Owner: postgres
--

CREATE SEQUENCE integracao_precos.usuario_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE integracao_precos.usuario_id_seq OWNER TO postgres;

--
-- TOC entry 3021 (class 0 OID 0)
-- Dependencies: 205
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: integracao_precos; Owner: postgres
--

ALTER SEQUENCE integracao_precos.usuario_id_seq OWNED BY integracao_precos.usuario.id;


--
-- TOC entry 2868 (class 2604 OID 18428)
-- Name: avaliacao id; Type: DEFAULT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.avaliacao ALTER COLUMN id SET DEFAULT nextval('integracao_precos.avaliacao_id_seq'::regclass);


--
-- TOC entry 2869 (class 2604 OID 18456)
-- Name: entrega id; Type: DEFAULT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.entrega ALTER COLUMN id SET DEFAULT nextval('integracao_precos.entrega_id_seq'::regclass);


--
-- TOC entry 2867 (class 2604 OID 18374)
-- Name: pagamento id; Type: DEFAULT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.pagamento ALTER COLUMN id SET DEFAULT nextval('integracao_precos.pagamentos_id_seq'::regclass);


--
-- TOC entry 2865 (class 2604 OID 18323)
-- Name: produto id; Type: DEFAULT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.produto ALTER COLUMN id SET DEFAULT nextval('integracao_precos.produto_id_seq'::regclass);


--
-- TOC entry 2866 (class 2604 OID 18366)
-- Name: usuario id; Type: DEFAULT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.usuario ALTER COLUMN id SET DEFAULT nextval('integracao_precos.usuario_id_seq'::regclass);


--
-- TOC entry 2877 (class 2606 OID 18433)
-- Name: avaliacao avaliacao_pkey; Type: CONSTRAINT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.avaliacao
    ADD CONSTRAINT avaliacao_pkey PRIMARY KEY (id);


--
-- TOC entry 2879 (class 2606 OID 18458)
-- Name: entrega entrega_pkey; Type: CONSTRAINT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.entrega
    ADD CONSTRAINT entrega_pkey PRIMARY KEY (id);


--
-- TOC entry 2875 (class 2606 OID 18376)
-- Name: pagamento pk_pagamentos_id; Type: CONSTRAINT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.pagamento
    ADD CONSTRAINT pk_pagamentos_id PRIMARY KEY (id);


--
-- TOC entry 2871 (class 2606 OID 18328)
-- Name: produto pk_produto_id; Type: CONSTRAINT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.produto
    ADD CONSTRAINT pk_produto_id PRIMARY KEY (id);


--
-- TOC entry 2881 (class 2606 OID 18474)
-- Name: produto_integracao pk_produto_integracao; Type: CONSTRAINT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.produto_integracao
    ADD CONSTRAINT pk_produto_integracao PRIMARY KEY (id_integracao, id_produto);


--
-- TOC entry 2873 (class 2606 OID 18368)
-- Name: usuario pk_usuario_id; Type: CONSTRAINT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.usuario
    ADD CONSTRAINT pk_usuario_id PRIMARY KEY (id);


--
-- TOC entry 2883 (class 2606 OID 18434)
-- Name: avaliacao fk_avaliacao_id_produto; Type: FK CONSTRAINT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.avaliacao
    ADD CONSTRAINT fk_avaliacao_id_produto FOREIGN KEY (id_produto) REFERENCES integracao_precos.produto(id);


--
-- TOC entry 2882 (class 2606 OID 18377)
-- Name: pagamento fk_pagamentos_produtos; Type: FK CONSTRAINT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.pagamento
    ADD CONSTRAINT fk_pagamentos_produtos FOREIGN KEY (id_produto) REFERENCES integracao_precos.produto(id);


--
-- TOC entry 2885 (class 2606 OID 18475)
-- Name: produto_integracao fk_produto_integracao_id_produto; Type: FK CONSTRAINT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.produto_integracao
    ADD CONSTRAINT fk_produto_integracao_id_produto FOREIGN KEY (id_produto) REFERENCES integracao_precos.produto(id);


--
-- TOC entry 2884 (class 2606 OID 18459)
-- Name: entrega fk_transportadora_id_produto; Type: FK CONSTRAINT; Schema: integracao_precos; Owner: postgres
--

ALTER TABLE ONLY integracao_precos.entrega
    ADD CONSTRAINT fk_transportadora_id_produto FOREIGN KEY (id_produto) REFERENCES integracao_precos.produto(id);


-- Completed on 2022-02-15 00:29:53 -03

--
-- PostgreSQL database dump complete
--



```
