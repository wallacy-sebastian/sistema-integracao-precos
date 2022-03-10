from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import time
import random
import json
from produto import Produto

class Produtos:
    user_agents = [
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36',
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36',
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) HeadlessChrome/91.0.4472.114 Safari/537.36',
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) HeadlessChrome/79.0.3945.88 Safari/537.36',
    ]
    
    def __init__(self) -> None:
        self.urls = []
        self.produtos = []

    def iniciarDriver(self):
        op = webdriver.ChromeOptions()

        user_agent = random.choice(self.user_agents)
        print(user_agent)

        op.add_argument('user-agent='+user_agent)

        srv=Service(ChromeDriverManager().install())
        self.driver = webdriver.Chrome(service=srv, options=op)

    def __testarAcesso(self, link, checkText = 'Access to this page has been denied.', checkSelector = ''):
        print(link)
        for attempt in range(5):
            self.driver.get(link)
            
            try:
                if checkSelector == '':
                    if self.driver.title == checkText:
                        time.sleep(5)
                    else:
                        time.sleep(1)
                        return True
                else:
                    try:
                        texto = self.driver(By.CSS_SELECTOR, checkSelector).text
                        if(texto == checkText):
                            time.sleep(5)
                    except:
                        time.sleep(1)
                        return True
            except:
                print(f'Número de tentativas para entrar na página: {attempt+1}')
                break

        return False

    def obterLinksDeProdutos(self, linkPagina, seletorLinks, checkText = 'Access to this page has been denied.', checkSelector = ''):
        self.urls = []
        if self.__testarAcesso(linkPagina, checkText, checkSelector):
            links_produto = self.driver.find_elements(By.CSS_SELECTOR, seletorLinks)
            for link_produto in links_produto:
                url = link_produto.get_attribute('href')
                self.urls.append(url)
        else:
            print('Não foi possível acessar a página')

    def obterProdutos(self, loja, secao, checkText = 'Access to this page has been denied.', checkSelector = ''):
        produto = Produto(loja, secao)
        for url in self.urls:
            #if url != 'https://www.colombo.com.br/produto/Informatica/Mouse-M350-910-005773-Sem-Fio-Logitech':
            #    continue
            if not self.__testarAcesso(url, checkText, checkSelector):
                continue
            produto.setUrl(url)
            try:
                info_produto = produto.obterProdutoInfo(self.driver)
                self.produtos.append(info_produto)
            except:
                if not produto.continuarExecucao():
                    print("Execução interrompida")
                    break

    def criarArquivo(self, caminho, modo = 'w'):
        arq = open(caminho, modo)
        arq.write(json.dumps(self.produtos, indent=4, ensure_ascii=False))
        arq.close()

    def liberarDriver(self):
        self.driver.quit()
