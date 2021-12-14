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

driver.get('https://www.kabum.com.br/produto/157553/monitor-lg-29-ips-ultra-wide-full-hd-hdmi-vesa-ajuste-de-angulo-hdr-10-99-srgb-freesync-29wl500')
print(driver.execute_script("return navigator.userAgent"))

driver.delete_all_cookies()

mais_pagamentos = driver.find_element(By.CSS_SELECTOR, '.sc-cSyqtw')
mais_pagamentos.click()
time.sleep(1)

## Parcelamentos
parcelamentos = []
opcoes_elementos = driver.find_elements(By.CSS_SELECTOR, '.sc-bjeSbO')


for opcao_elemento in opcoes_elementos:

    ### Opção de parcelamento

    #opcao_elemento = driver.find_element(By.CSS_SELECTOR, '.header__Item-wr8kge-1.gyjwue span')
    opcao_elemento.click()
    time.sleep(0.5)
    opcao_parcelamento = opcao_elemento.get_attribute('innerHTML')

    ### Parcelamento tabela
    linhas = driver.find_elements(By.CSS_SELECTOR, 'ul.sc-hRMWxn > li')
    if len(linhas) == 0:
        info_elemento = driver.find_elements(By.CSS_SELECTOR, '.sc-hgKiOD span')
        infos = info_elemento[0].text + info_elemento[1].text
    else:
        infos = ''

    parcelamento = []

    for linha in linhas:
        tds = linha.find_elements(By.XPATH, './child::*')

        parcelamento.append({
            'qtd': tds[0].get_attribute('innerHTML').split('x')[0],
            'valor_parcela': tds[0].get_attribute('innerHTML').split(' ')[-1].replace(',', '.'),
            'juros': 'sem juros',
            'total': tds[1].get_attribute('innerHTML')[10:].replace(',', '.'),
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

arq = open('../../json/kabum_produto.json', 'w')
arq.write(json_parcelamentos)
arq.close()

driver.quit()
