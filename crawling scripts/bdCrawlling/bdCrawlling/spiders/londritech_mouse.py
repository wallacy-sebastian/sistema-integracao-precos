import scrapy
from datetime import datetime

class KabumSpider(scrapy.Spider):
    name = "londritech_mouse"
    start_urls = [
        'https://londritech.com.br/b?cid=5036%2F5074%2F&cn=PERIF%C3%89RICOS%2FMOUSE&pg=1',
    ]
    pn = 1
    secao = ''

    def parse(self, response):
        produtos = response.css('.product-main .showcase__list .product-result')

        self.secao = response.css('.breadcrumb-header_list h1::text').get()

        for produto in produtos:
            a = produto.css('a.showcase-product__link::attr(href)').get()
            if a is not None:
                yield response.follow(a, callback=self.product_page)
        #obtendo a proxima pagina
        if self.pn <= 5:
            self.pn += 1
            next_page = self.start_urls[0][0:-1] + str(self.pn)
            #chamado recursiva para proxima pagina
            yield response.follow(next_page, callback=self.parse)
        else:
            self.pn = 1

    def product_page(self, response):
        created_at = datetime.now()

        #produto
        nome = response.css('header .product-header__title::text').get()
        valor = response.css('.product-price .product-price-primary strong span::text')[1].get()
        valor.strip()
        marca = response.css('.product-descriptions__resume').re(r'Fabricante: .*')
        if marca == '':
            marca = response.css('.product-descriptions__resume').re(r'Marca: .*')
        modelo = response.css('.product-descriptions__resume').re(r'Modelo: .*')
        descricao = response.css('.product-descriptions__resume::text').get()
        url_img = response.css('.product-content .product-zoom img::attr(src)').get()
        
        yield {
            'produto': {
                'nome': nome,
                'valor': valor,
                'descricao': descricao,
                'modelo': modelo,
                'url_img': url_img,
                'createdAt': created_at,
                'secao': 1
            },
        }