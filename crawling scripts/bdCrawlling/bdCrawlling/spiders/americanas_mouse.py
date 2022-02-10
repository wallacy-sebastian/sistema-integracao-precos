import scrapy
import time
from random import randrange
from datetime import datetime
from bs4 import BeautifulSoup
# from selenium import webdriver
import json

class AmericanasMouseSpider(scrapy.Spider):
    name = "americanas_mouse"
    start_urls = [
        'https://www.americanas.com.br/categoria/informatica-e-acessorios/perifericos/mouse',
    ]
    pn = 0
    secao = ''

    def parse(self, response):
        produtos = response.css('.middle-area__WrapperRight-sc-1k81b14-0 .grid__StyledGrid-sc-1man2hx-0 .col__StyledCol-sc-1snw5v3-0')
        self.secao = response.css('#listingHeader #headerName h1::text').get()
        for produto in produtos:
            a = produto.css('div div a::attr(href)').get()
            if a is not None:
                yield response.follow(a, callback=self.product_page)
        # obtendo a proxima pagina
        time.sleep(2)
        if self.pn <= 144:
            self.pn += 24
            next_page = self.start_urls[0] + '?limit=24&offset=' + str(self.pn)

            #chamado recursiva para proxima pagina
            yield response.follow(next_page, callback=self.parse)


    def product_page(self, response):
        created_at = datetime.now()
        secao = 1
        #produto
        nome = response.css('.src__Container-dda50e-0 .product-info__Cell-sc-1u2zqg7-2 h1::text').get()
        valor = response.css('.src__Container-dda50e-0 .src__BestPrice-sc-1jvw02c-5').re(r'[0-9]+,[0-9]+')
        descricao = response.css('.src__Container-dda50e-0 .product-description__Description-ytj6zc-1::text').get()
        url_img = response.css('.src__Container-dda50e-0 picture img::attr(src)').get()
        html_table = response.css('.src__AccordionBoxWrapper-sc-70o4ee-8 table').get()

        table_data = [[cell.text for cell in row("td")]
                        for row in BeautifulSoup(html_table)("tr")]
        info_tec = json.dumps(dict(table_data))

        #avaliacao
        av = response.css('#card-reviews-title .src__Container-sc-14ufm2e-0 .kTaskQ')
        av_list = []
        for ava in av:
            comentario = ava.css('.ealAWO::text').get()
            nome1 = ava.css('.ccHGTh::text').get()
            data_ava = ava.css('.ccHGTh::text')[2].get()
            estrela = randrange(0, 6)
            dic = {
                'comentario': comentario,
                'nome': nome1,
                'data_avaliacao': data_ava,
                'estrelas': estrela
            }
            av_list.append(dic)

        if av_list is []:
            av_list = None



        da = response.css('#card-reviews-title .review__Flex-sc-1cmx3pv-0')
                
        #info_tec = response.css('#secaoInformacoesTecnicas').re(r'Marca.*') -- Não alcançavel pelo scrapy
        #pagamento e entrega -- não é possivel acessar pois é feita via ajax, o scrap nao captura
        yield {
            'product': {
                'nome': nome,
                'valor': valor[0],
                'descricao': descricao,
                'info': info_tec,
                'url_img': url_img,
                'data_coleta': created_at,
                'secao': secao,
            },
            'avaliacao': av_list
        }

        