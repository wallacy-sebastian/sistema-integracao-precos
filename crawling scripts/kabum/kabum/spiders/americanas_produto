from selenium import webdriver
from selenium.webdriver.common.by import By
from datetime import datetime
import json
import time
import re

#PATH = '/home/wallacy/Documentos/Faculdade/Banco de dados/trab-equipe/chromedriver_linux64/chromedriver'
#op = webdriver.ChromeOptions()
#op.add_argument('user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36')
#driver = webdriver.Chrome(executable_path=PATH, options=op)

PATH = '../../../drivers/geckodriver'
op = webdriver.FirefoxOptions()
op.headless = True
op.add_argument('user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36')
driver = webdriver.Firefox(executable_path=PATH, options=op)

driver.get('https://www.americanas.com.br/produto/1076473517')
print(driver.execute_script("return navigator.userAgent"))

driver.delete_all_cookies()

mais_pagamentos = driver.find_element(By.CSS_SELECTOR, '.src__MorePaymentMethods-sc-1urqdh5-2')
mais_pagamentos.click()
time.sleep(1)

## Parcelamentos
parcelamentos = []
opcoes_elementos = driver.find_elements(By.CSS_SELECTOR, '.header__Item-wr8kge-1 span')


for opcao_elemento in opcoes_elementos:

    ### Opção de parcelamento

    #opcao_elemento = driver.find_element(By.CSS_SELECTOR, '.header__Item-wr8kge-1.gyjwue span')
    opcao_elemento.click()
    time.sleep(0.5)
    opcao_parcelamento = opcao_elemento.get_attribute('innerHTML')

    ### Parcelamento tabela
    linhas = driver.find_elements(By.CSS_SELECTOR, 'table.table__TableUI-sc-5d4ssj-6 > tbody > tr')
    if len(linhas) == 0:
        info_elemento = driver.find_element(By.CSS_SELECTOR, '.table__TableWrapper-sc-5d4ssj-0')
        infos = info_elemento.text
    else:
        infos = ''

    parcelamento = []

    for linha in linhas:
        tds = linha.find_elements(By.XPATH, './child::*')

        parcelamento.append({
            'qtd': tds[0].get_attribute('innerHTML')[:-1],
            'valor_parcela': tds[1].get_attribute('innerHTML')[6:].replace(',', '.'),
            'juros': tds[2].get_attribute('innerHTML'),
            'total': tds[3].get_attribute('innerHTML')[9:].replace(',', '.'),
        })

    parcelamentos.append({
        'tipo_de_pagamento': opcao_parcelamento,
        'informacoes': infos,
        'parcelas': parcelamento,
    })


# Entidade pagamento
pagamento = {
    'data_coleta': datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
    'tipos_de_pagamento': parcelamentos
}

json_parcelamentos = json.dumps(pagamento, indent=4)

arq = open('../../json/americanas_produto.json', 'w')
arq.write(json_parcelamentos)
arq.close()

driver.quit()
