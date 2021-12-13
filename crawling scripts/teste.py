import time
import json
import re

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By

options = Options()
options.headless = True



driver = webdriver.Chrome('chromedriver',  chrome_options=options)  # Optional argument, if not specified will search path.

driver.get('https://www.kabum.com.br/produto/128719/mouse-multilaser-classic-box-optico-full-black-mo300');

time.sleep(5) # Let the user actually see something!

re1 = re.compile(r'Marca: .*')
re2 = re.compile(r'Modelo: .*')
# marca = driver.find_element_by_xpath('//section[@id="secaoInformacoesTecnicas"]')
marca = driver.find_element(By.XPATH, '//section[@id="secaoInformacoesTecnicas"]/div[contains(.,"Marca")]')
# modelo = driver.find_element_by_xpath('//section[@id="secaoInformacoesTecnicas"]')
modelo = driver.find_element(By.XPATH, '//section[@id="secaoInformacoesTecnicas"]/div[contains(.,"Modelo")]')
print(marca, modelo)
# d =  {
#     "marca": marca,
#     "modelo": modelo
# }

# json.dump(d, open('teste.json', 'w'))

# Let the user actually see something!

driver.quit()
