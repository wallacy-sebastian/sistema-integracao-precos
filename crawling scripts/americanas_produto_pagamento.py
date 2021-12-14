from selenium import webdriver
from selenium.webdriver.common.by import By
from datetime import datetime
import json
import time
import html

#PATH = '/home/wallacy/Documentos/Faculdade/Banco de dados/trab-equipe/chromedriver_linux64/chromedriver'
#op = webdriver.ChromeOptions()
#op.add_argument('user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36')
#driver = webdriver.Chrome(executable_path=PATH, options=op)

PATH = '/home/wallacy/Documentos/Faculdade/Banco de dados/trab-equipe/geckodriver_v0.30.0_linux64/geckodriver'
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

# Opção de parcelamento

opcao_elemento = driver.find_element(By.CSS_SELECTOR, '.header__Item-wr8kge-1.gyjwue span')
opcao_parcelamento = opcao_elemento.get_attribute('innerHTML')

# Parcelamento tabela
linhas = driver.find_elements(By.CSS_SELECTOR, 'table.table__TableUI-sc-5d4ssj-6 > tbody > tr')

parcelamento = []

for linha in linhas:
    tds = linha.find_elements(By.XPATH, './child::*')

    parcelamento.append({
        'qtd': tds[0].get_attribute('innerHTML'),
        'valor_parcela': tds[1].get_attribute('innerHTML'),
        'juros': tds[2].get_attribute('innerHTML'),
        'total': tds[3].get_attribute('innerHTML'),
    })


# Entidade pagamento
pagamento = {
    'data_coleta': datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
    'opcao_parcelamento': opcao_parcelamento,
    'parcelamentos': parcelamento
}

json_parcelamentos = json.dumps(pagamento, indent=4)

arq = open('kabum/json/americanas_produto.json', 'w')
arq.write(json_parcelamentos)
arq.close()

driver.quit()
