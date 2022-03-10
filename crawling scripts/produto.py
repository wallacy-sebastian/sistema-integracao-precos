from datetime import datetime
import time
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select

# Lojas:
#       1 - Americanas
#       2 - Kabum
#       3 - Londritech
# Seções:
#       1 - Mouse
#       2 - Monitor

class Produto:
    elementos = {
        "nome": [
            ".nome-produto",
            "body > div > main > article > section > div:nth-child(2) > h1",
            ".product-header > h1"
        ],
        "valor": [
            ".dados-preco-valor--label",
            "#blocoValores > div:nth-child(2) > div > h4",
            ".product-price > div > strong > span:last-child"
        ],
        "descricao": [
            ".detalhe-descricao",
            "#description",
            ".product-descriptions > div:last-child > div:first-child > div > section:first-child > div"
        ],
        "fichaTecnica": [
            "#produto-caracteristicas > div",
            "#secaoInformacoesTecnicas > div > div:last-child",
            ".product-descriptions > div:last-child > div:first-child > div > section:first-child > div"
        ],
        "urlImg": [
            ".carrossel-fotos-produto__imagem",
            "body > div > main > article > section > div:nth-child(2) > div > div:first-child > div:nth-child(2) > div:last-child img",
            "#product-zoom-image"
        ],
        "modelo": [
            "#produto-caracteristicas > div",
            "#secaoInformacoesTecnicas > div > div:last-child",
            ".product-descriptions > div:last-child > div:first-child > div > section:first-child > div"
        ],
        "marca": [
            "",
            "body > div > main > article > section > div:nth-child(2) > div > div:last-child > section > div > h4:last-child > b",
            ""
        ],
        "avaliacoes": [
            {
                "lista": ".avaliacoes-listagem-avaliacao",
                "dados": {
                    "nome": ":scope > .avaliacao-info > .avaliacao-autor > strong",
                    "comentario": ":scope > .avaliacao-comentario",
                    "data": ":scope > .avaliacao-info > .avaliacao-data > span",
                    "estrelas": ":scope > .avaliacao-info > .avaliacao-estrelas > .estrelas-avaliacao"
                },
            },
            {
                "lista": "#secaoAvaliacoes > div > div:last-child > div > div:nth-child(2) > div",
                "dados": {
                    "nome": ":scope > div:nth-of-type(2)",
                    "comentario": ":scope > div:nth-child(n+3)",
                    "data": ":scope > div:first-child > div:first-child > span:last-child",
                    "estrelas": ":scope > div:first-child > div:last-child > div > div > svg > path"
                },
            },
            {
                "lista": "",
                "dados": {
                    "nome": "",
                    "comentario": "",
                    "data": "",
                    "estrelas": ""
                },
            }
        ],
        "entregas": [
            {
                "acesso": ".dados-produto-botao-servico > .frete-produto-link",
                "buscaCEP": ".cep-detalhe.form-control",
                "lista": "#dados-produto-disponivel .frete-produto-resultado-table tbody tr",
                "dados": {
                    "nome": ":scope > td:first-child",
                    "valor": ":scope > td:nth-child(2)"
                },
                "fechar": "#dados-produto-disponivel .frete-produto-resultado-table tbody tr"
            },
            {
                "buscaCEP": "#formularioCalcularFrete > input",
                "lista": "#listaOpcoesFrete > div",
                "dados": {
                    "nome": ":scope > div:first-child > span",
                    "valor": ":scope > div:last-child > span:first-child"
                },
                "fechar": "#modalWrapper > div > div:first-child > div:last-child"
            },
            {
                "buscaCEP": ".product-content .product-middle .product-shipping input:first-child",
                "lista": ".product-content .product-middle .product-shipping table tbody tr",
                "dados": {
                    "nome": ":scope > td:first-child",
                    "valor": ":scope > td:last-child"
                },
                "fechar": ".product-content .product-middle .product-shipping input:first-child"
            },
        ],
        "pagamentos": [
            {
                "acesso": ".dados-preco-link-parcelas",
                "cartaoDeCredito": {
                    "acesso": "#dados-produto-disponivel .parcelas-produto--sem-juros .parcelas-produto-table",
                    "lista": "#dados-produto-disponivel .parcelas-produto--sem-juros .parcelas-produto-table > tbody > tr:not(:last-child)",
                    "dados": {
                        "vezes": ":scope > td:first-child",
                        "valor": ":scope > td:nth-child(2)"
                    }
                },
                "boleto": {
                    "acesso": ".dados-preco-valor--label",
                    "dados": {
                        "valor": ".dados-preco-valor--label"
                    }
                },
                "PIX": {
                    "acesso": ".dados-preco-valor--label",
                    "dados": {
                        "valor": ".dados-preco-valor--label"
                    }
                },
            },
            {
                "acesso": "#blocoValores > div:last-child > button",
                "cartaoDeCredito": {
                    "acesso": "#modalWrapper > div > div:nth-child(2) > button:nth-child(1)",
                    "lista": "#modalWrapper > div > div:nth-child(3) > ul > li",
                    "dados": {
                        "vezes": ":scope > span:first-child",
                        "valor": ":scope > span:last-child"
                    }
                },
                "boleto": {
                    "acesso": "#modalWrapper > div > div:nth-child(2) > button:nth-child(2)",
                    "dados": {
                        "valor": "#modalWrapper > div > div:nth-child(3) > span:first-child"
                    }
                },
                "PIX": {
                    "acesso": "#modalWrapper > div > div:nth-child(2) > button:nth-child(3)",
                    "dados": {
                        "valor": "#modalWrapper > div > div:nth-child(3) > span:first-child"
                    }
                },
            },
            {
                "acesso": ".product-payments__link",
                "cartaoDeCredito": {
                    "acesso": "",
                    "lista": "",
                    "dados": {
                        "vezes": "",
                        "valor": ""
                    }
                },
                "boleto": {
                    "acesso": "#prod-payments__select",
                    "lista": ".prod-payments__box.prod-payments__2 > .prod-payments__item",
                    "dados": {
                        "vezes": ":scope > span:first-child",
                        "valor": ":scope > span:last-child"
                    }
                },
                "PIX": {
                    "acesso": "",
                    "dados": {
                        "valor": ""
                    }
                },
                "deposito": {
                    "acesso": "#prod-payments__select",
                    "dados": {
                        "valor": ".prod-payments__box.prod-payments__0 > .prod-payments__item > span:last-child"
                    }
                },
            }
        ]
    }

    continuar = False

    def __init__(self, loja, secao) -> None:
        self.loja = loja
        self.secao = secao

        self.nome = ""
        self.valor = 0
        self.descricao = ""
        self.fichaTecnica = ""
        self.urlImg = ""
        self.modelo = ""
        self.marca = ""

    def continuarExecucao(self):
        return self.continuar

    def __corrigirValor(self, valor_texto):
        try:
            valor_texto = valor_texto if valor_texto.find(".") == -1 else valor_texto.replace(".", "", 1)
            valor_corrigido = float(valor_texto.replace(",", ".", 1))

            return valor_corrigido
        except:
            print("Erro ao corrigir o valor: " + valor_texto)
            raise

    def setUrl(self, url):
        self.url = url
        self.continuar = False

    def obterData(self):
        self.data = datetime.today().strftime('%Y-%m-%d %H:%M:%S')

    def obterNome(self, driver):
        try:
            self.nome = driver.find_element(By.CSS_SELECTOR, self.elementos["nome"][self.loja-1]).get_attribute('textContent')
            if self.loja == 1:
                self.nome = self.nome[:self.nome.rfind("(")]
        except:
            print("Não foi possível capturar o nome.")
            raise

    def obterValor(self, driver):
        try:
            valor_texto = driver.find_element(By.CSS_SELECTOR, self.elementos["valor"][self.loja-1]).get_attribute('textContent')
            if self.loja == 2:
                valor_texto = valor_texto[3:]
            self.valor = self.__corrigirValor(valor_texto)
        except:
            print("Não foi possível capturar o valor.")
            raise

    def obterDescricao(self, driver):
        try:
            descricao = driver.find_element(By.CSS_SELECTOR, self.elementos["descricao"][self.loja-1]).get_attribute('innerText')

            if self.loja == 1 or self.loja == 2:
                self.descricao = descricao
            elif self.loja == 3:
                index = self.descricao.find("Especificações:")
                if index != -1:
                    self.descricao = descricao[:index]
                else:
                    self.descricao = descricao
            else:
                pass
        except:
            print("Não foi possível capturar a descrição.")
            raise

    def obterFichaTecnica(self, driver):
        try:
            fichaTecnica = driver.find_element(By.CSS_SELECTOR, self.elementos["fichaTecnica"][self.loja-1]).get_attribute("innerText")

            if self.loja == 1:
                self.fichaTecnica = fichaTecnica.replace("\t", " ").strip()
            elif self.loja == 2:
                self.fichaTecnica = fichaTecnica
            elif self.loja == 3:
                index = self.fichaTecnica.find("Especificações:")
                if index != -1:
                    self.fichaTecnica = fichaTecnica[(index+15):]
                else:
                    self.fichaTecnica = fichaTecnica
            else:
                pass
        except:
            if self.loja != 1:
                print("Não foi possível capturar a ficha técnica.")
                raise

    def obterUrlImg(self, driver):
        try:
            self.urlImg = driver.find_element(By.CSS_SELECTOR, self.elementos["urlImg"][self.loja-1]).get_attribute('src')
        except:
            print("Não foi possível capturar a URL da imagem.")
            raise

    def obterModelo(self, driver):
        try:
            if self.fichaTecnica.find("Modelo") != -1:
                self.modelo = driver.find_element(By.CSS_SELECTOR, self.elementos["modelo"][self.loja-1]).get_attribute('innerHTML')
                index = self.modelo.find("Modelo")
                if self.loja == 1:
                    index = self.fichaTecnica.find("Modelo")
                    index_fim = self.fichaTecnica[index:].find("\n")
                    if index_fim == -1:
                        self.modelo = self.fichaTecnica[index+7:]
                    else:
                        self.modelo = self.fichaTecnica[index+7:index_fim]
                elif self.loja == 2:
                    index_fim = self.modelo[index:].find("<")
                    self.modelo = self.modelo[(index+7):(index+index_fim)]
                elif self.loja == 3:
                    self.modelo = self.modelo[index:index+50].replace("</strong>", "", 1)
                    index_fim = self.modelo.find("<")
                    self.modelo = self.modelo[7:index_fim]
                else:
                    pass
                self.modelo = self.modelo.replace("&nbsp;", " ", 1).strip()
            elif self.descricao.find("Modelo") != -1:
                self.modelo = driver.find_element(By.CSS_SELECTOR, self.elementos["descricao"][self.loja-1]).get_attribute('innerHTML')
                index = self.modelo.find("Modelo")
                if self.loja == 1:
                    if self.modelo[index+7] == "<":
                        self.modelo = self.modelo[index:index+50].replace("<br>", "", 1)
                    else:
                        self.modelo = self.modelo[index:index+50]
                    index_fim = self.modelo.find("<")
                    self.modelo = self.modelo[7:index_fim]
            self.modelo = self.modelo.strip("-").strip()
        except:
            print("Não foi possível capturar o modelo.")
            raise

    def obterMarca(self, driver):
        try:
            if self.loja == 1:
                if self.fichaTecnica.find("Marca") != -1:
                    self.marca = driver.find_element(By.CSS_SELECTOR, self.elementos["fichaTecnica"][self.loja-1]).get_attribute('innerHTML')
                    index = self.marca.find("Marca")
                    self.marca = self.marca[index:index+50].replace("</b>", "", 1)
                    index_fim = self.marca.find("<")
                    self.marca = self.marca[6:index_fim]
                elif self.descricao.find("Marca") != -1:
                    self.marca = driver.find_element(By.CSS_SELECTOR, self.elementos["descricao"][self.loja-1]).get_attribute('innerHTML')
                    index = self.marca.find("Marca")
                    if self.marca[index+6] == "<":
                        self.marca = self.marca[index:index+50].replace("<br>", "", 1)
                    else:
                        self.marca = self.marca[index:index+50]
                    index_fim = self.marca.find("<")
                    self.marca = self.marca[6:index_fim]
            elif self.loja == 2:
                try:
                    self.marca = driver.find_element(By.CSS_SELECTOR, self.elementos["marca"][self.loja-1]).text
                except:
                    if self.fichaTecnica.find("Marca") != -1:
                        self.marca = driver.find_element(By.CSS_SELECTOR, self.elementos["fichaTecnica"][self.loja-1]).get_attribute('innerHTML')
                        index = self.marca.find("Marca")
                        index_fim = self.marca[index:].find("<")
                        self.marca = self.marca[(index+6):(index+index_fim)]
            elif self.loja == 3:
                if self.fichaTecnica.find("Marca") != -1:
                    self.marca = driver.find_element(By.CSS_SELECTOR, self.elementos["fichaTecnica"][self.loja-1]).get_attribute('innerHTML')
                    index = self.marca.find("Marca")
                    self.marca = self.marca[index:index+50].replace("</strong>", "", 1)
                    index_fim = self.marca.find("<")
                    self.marca = self.marca[6:index_fim]
            else:
                pass
            self.marca = self.marca.replace("&nbsp;", " ", 1).strip("-").strip()
        except:
            print("Não foi possível capturar a marca.")
            raise

    def obterAvaliacoes(self, driver):
        try:
            self.avaliacoes = []

            if self.loja == 3:
                return

            avaliacoes_elemento = driver.find_elements(By.CSS_SELECTOR, self.elementos["avaliacoes"][self.loja-1]["lista"])

            for avaliacao_elemento in avaliacoes_elemento:
                avaliacao = {
                    "nome": "",
                    "comentario": "",
                    "data": "",
                    "estrelas": 0
                }

                avaliacao["nome"] = avaliacao_elemento.find_element(
                    By.CSS_SELECTOR, self.elementos["avaliacoes"][self.loja-1]["dados"]["nome"]).get_attribute('textContent')
                avaliacao["data"] = avaliacao_elemento.find_element(
                    By.CSS_SELECTOR, self.elementos["avaliacoes"][self.loja-1]["dados"]["data"]).get_attribute('textContent')

                if self.loja == 1:
                    avaliacao["comentario"] = avaliacao_elemento.find_element(
                        By.CSS_SELECTOR, self.elementos["avaliacoes"][self.loja-1]["dados"]["comentario"]).get_attribute('innerText')
                    estrelas_avaliacao = avaliacao_elemento.find_elements(By.CSS_SELECTOR, self.elementos["avaliacoes"][self.loja-1]["dados"]["estrelas"])
                    estrelas = estrelas_avaliacao.find_elements(By.CSS_SELECTOR, ":scope > span.ci-star-full")
                    avaliacao["estrelas"] = len(estrelas)
                elif self.loja == 2:
                    avaliacao["data"] = avaliacao["data"][1:-1]
                    _comentario = avaliacao_elemento.find_elements(
                        By.CSS_SELECTOR, self.elementos["avaliacoes"][self.loja-1]["dados"]["comentario"])

                    avaliacao["comentario"] = ""
                    for comentario in _comentario:
                        avaliacao["comentario"] += " " + comentario.get_attribute('textContent')

                    estrelas = avaliacao_elemento.find_elements(By.CSS_SELECTOR, self.elementos["avaliacoes"][self.loja-1]["dados"]["estrelas"])
                    avaliacao["estrelas"] = 0
                    for estrela in estrelas:
                        avaliacao["estrelas"] += 1 if (estrela.get_attribute('d')[:3] == "M17") else 0
                else:
                    pass

                self.avaliacoes.append(avaliacao)
        except:
            print("Não foi possível capturar as informações das avaliações.")
            raise

    def obterEntregas(self, driver):
        try:
            self.entregas = []
            time.sleep(2)
            if self.loja == 1:
                buscaCEP = driver.find_element(By.CSS_SELECTOR, self.elementos["entregas"][self.loja-1]["acesso"])
                buscaCEP.click()
            buscaCEP = driver.find_element(By.CSS_SELECTOR, self.elementos["entregas"][self.loja-1]["buscaCEP"])
            while buscaCEP.get_attribute('value') in ['_____-___', ''] :
                buscaCEP.clear()
                for b in '86185530':
                    buscaCEP.send_keys(b)
                    time.sleep(0.005)
                time.sleep(0.5)

            buscaCEP.send_keys(Keys.RETURN)
            if self.loja == 1 or self.loja == 2:
                time.sleep(2)
            elif self.loja == 3:
                time.sleep(6)
            else:
                pass

            entregas_elemento = driver.find_elements(By.CSS_SELECTOR, self.elementos["entregas"][self.loja-1]["lista"])

            for entrega_elemento in entregas_elemento:
                entrega = {
                    "nome": "",
                    "valor": 0
                }

                entrega["nome"] = entrega_elemento.find_element(
                    By.CSS_SELECTOR, self.elementos["entregas"][self.loja-1]["dados"]["nome"]).get_attribute('textContent')

                valor_texto = entrega_elemento.find_element(
                    By.CSS_SELECTOR, self.elementos["entregas"][self.loja-1]["dados"]["valor"]).get_attribute('textContent')[3:]
                entrega["valor"] = self.__corrigirValor(valor_texto)

                self.entregas.append(entrega)

            entregas_fechar = driver.find_element(By.CSS_SELECTOR, self.elementos["entregas"][self.loja-1]["fechar"])
            entregas_fechar.click()
            time.sleep(1)
        except:
            print("Não foi possível capturar as informações das entregas.")
            self.continuar = True
            raise

    def obterPagamentosCartaoDeCredito(self, driver):
        try:
            acessoTipoPagamento = driver.find_element(By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["cartaoDeCredito"]["acesso"])
            acessoTipoPagamento.click()
            time.sleep(1)

            parcelas_elemento = driver.find_elements(By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["cartaoDeCredito"]["lista"])

            for parcela_elemento in parcelas_elemento:
                parcela = {
                    "tipo": 1,
                    "vezes": 1,
                    "valor": 0
                }

                vezes_texto = parcela_elemento.find_element(
                    By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["cartaoDeCredito"]["dados"]["vezes"]).get_attribute('textContent')
                if self.loja == 1:
                    valor_texto = parcela_elemento.find_element(
                        By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["cartaoDeCredito"]["dados"]["valor"]).get_attribute('textContent')[3:]
                    valor_total = self.__corrigirValor(valor_texto)
                    
                    parcela["vezes"] = int(vezes_texto[:vezes_texto.find("x")]) if vezes_texto[:1].isnumeric() else 1
                    parcela["valor"] = valor_total
                else:
                    valor_texto = parcela_elemento.find_element(
                        By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["cartaoDeCredito"]["dados"]["valor"]).get_attribute('textContent')[10:]
                    valor_total = self.__corrigirValor(valor_texto)
                    
                    parcela["vezes"] = int(vezes_texto[:vezes_texto.find("x")]) if vezes_texto[:1].isnumeric() else 1
                    parcela["valor"] = round(valor_total / parcela["vezes"], 2)

                self.pagamentos.append(parcela)
        except:
            print("Não foi possível capturar as informações de cartão de crédito.")
            raise

    def obterPagamentosBoleto(self, driver):
        try:
            acessoTipoPagamento = driver.find_element(By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["boleto"]["acesso"])

            if self.loja == 3:
                Select(acessoTipoPagamento).select_by_index(2)
                time.sleep(1)
                parcelas_elemento = driver.find_elements(By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["boleto"]["lista"])

                for parcela_elemento in parcelas_elemento:
                    parcela = {
                        "tipo": 2,
                        "vezes": 1,
                        "valor": 0
                    }

                    vezes_texto = parcela_elemento.find_element(
                        By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["boleto"]["dados"]["vezes"]).get_attribute('textContent')

                    valor_texto = parcela_elemento.find_element(
                        By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["boleto"]["dados"]["valor"]).get_attribute('textContent')[10:]
                    valor_total = self.__corrigirValor(valor_texto)
                    
                    parcela["vezes"] = int(vezes_texto[:vezes_texto.find("x")]) if vezes_texto[:1].isnumeric() else 1
                    parcela["valor"] = round(valor_total / parcela["vezes"], 2)

                    self.pagamentos.append(parcela)
            else:
                parcela = {
                    "tipo": 2,
                    "vezes": 1,
                    "valor": 0
                }
                acessoTipoPagamento.click()
                time.sleep(1)
                if self.loja == 1:
                    valor_texto = driver.find_element(
                        By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["boleto"]["dados"]["valor"]).get_attribute('textContent')
                else:
                    valor_texto = driver.find_element(
                        By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["boleto"]["dados"]["valor"]).get_attribute('textContent')[2:]
                parcela["valor"] = self.__corrigirValor(valor_texto)

                self.pagamentos.append(parcela)
        except:
            print("Não foi possível capturar as informações de boleto.")
            raise

    def obterPagamentosPIX(self, driver):
        try:
            parcela = {
                "tipo": 3,
                "vezes": 1,
                "valor": 0
            }

            acessoTipoPagamento = driver.find_element(By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["PIX"]["acesso"])
            acessoTipoPagamento.click()
            time.sleep(1)
            valor_texto = driver.find_element(
                By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["PIX"]["dados"]["valor"]).get_attribute('textContent')[2:]
            parcela["valor"] = self.__corrigirValor(valor_texto)

            self.pagamentos.append(parcela)
        except:
            print("Não foi possível capturar as informações de PIX.")
            raise

    def obterPagamentosDeposito(self, driver):
        try:
            parcela = {
                "tipo": 4,
                "vezes": 1,
                "valor": 0
            }

            acessoTipoPagamento = driver.find_element(By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["deposito"]["acesso"])
            Select(acessoTipoPagamento).select_by_index(0)

            time.sleep(1)
            valor_texto = driver.find_element(
                By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["deposito"]["dados"]["valor"]).get_attribute('textContent')[10:]
            parcela["valor"] = self.__corrigirValor(valor_texto)

            self.pagamentos.append(parcela)
        except:
            print("Não foi possível capturar as informações de depósito.")
            raise

    def obterPagamentos(self, driver):
        try:
            self.pagamentos = []
            try:
                acessoPagamentos = driver.find_element(By.CSS_SELECTOR, self.elementos["pagamentos"][self.loja-1]["acesso"])
                acessoPagamentos.click()
            except:
                if self.loja == 3:
                    esgotado = driver.find_element(By.CSS_SELECTOR, ".product-middle").get_attribute("textContent")
                    if "Produto esgotado" in esgotado:
                        print("Produto esgotado")
                        self.continuar = True
                        raise
                else:
                    raise
            time.sleep(2)

            self.obterPagamentosBoleto(driver)

            if self.loja == 1 or self.loja == 2:
                self.obterPagamentosCartaoDeCredito(driver)
                self.obterPagamentosPIX(driver)
            elif self.loja == 3:
                self.obterPagamentosDeposito(driver)
            else:
                pass
        except:
            print("Não foi possível capturar as informações dos pagamentos.")
            raise

    def obterProdutoInfo(self, driver):
        self.obterData()
        self.obterNome(driver)
        self.obterValor(driver)
        self.obterDescricao(driver)
        self.obterFichaTecnica(driver)
        self.obterUrlImg(driver)
        self.obterModelo(driver)
        self.obterMarca(driver)
        self.obterAvaliacoes(driver)
        self.obterEntregas(driver)
        self.obterPagamentos(driver)

        info_produto = {
            "produto": {
                "createdAt": self.data,
                "secao": self.secao,
                "url": self.url,
                "nome": self.nome,
                "valor": self.valor,
                "descricao": self.descricao,
                "fichaTecnica": self.fichaTecnica,
                "urlImg": self.urlImg,
                "modelo": self.modelo,
                "marca": self.marca,
                "loja": self.loja
            },
            "avaliacao": self.avaliacoes,
            "entrega": self.entregas,
            "pagamento": self.pagamentos
        }

        return info_produto
