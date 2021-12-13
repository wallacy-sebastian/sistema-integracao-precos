import scrapy
from datetime import datetime

class KabumSpider(scrapy.Spider):
    name = "kabum_monitor"
    start_urls = [
        'https://www.kabum.com.br/computadores/monitores',
    ]
    page_url = '?page_number='
    pn = 1
    secao = ''

    def parse(self, response):
        produtos = response.css('main div.productCard')
        self.secao = response.css('#listingHeader #headerName h1::text').get()
        for produto in produtos:
            a = produto.css('a::attr(href)').get()
            if a is not None:
                yield response.follow(a, callback=self.product_page)
        #obtendo a proxima pagina
        if self.pn <= 5:
            self.pn += 1
            next_page = self.page_url + str(self.pn)
            #chamado recursiva para proxima pagina
            yield response.follow(next_page, callback=self.parse)
        else:
            self.pn = 1


    def product_page(self, response):
        created_at = datetime.now()
        secao = response.css('#headerName h1::text').get()

        #produto
        pd = response.css('#__next main article section div')[1]
        nome = pd.css('h1::text').get()
                        # valor = pd.css('div div #blocoValores h4[itemprop="price"]::text').get()
        valor = pd.css('b::text').re(r'[0-9]+,[0-9]+')[0]
        descricao = response.css('#iframeContainer #description::text').get()
        if descricao == '':
            descricao = response.css('#iframeContainer #description p::text').get()
        url_img = response.css('#carouselDetails figure.iiz img::attr(src)').get()
        
        #info_tec = response.css('#secaoInformacoesTecnicas').re(r'Marca.*') -- Não alcançavel pelo scrapy
        #pagamento e entrega -- não é possivel acessar pois é feita via ajax, o scrap nao captura
        yield {
            'data_coleta': created_at,
            'secao': secao,
            'produto': {
                'nome': nome,
                'valor': valor,
                'descricao': descricao,
                #'info': info_tec,
                'url_img': url_img
            },
        }