from datetime import datetime
from produtos import Produtos

caminhoJson = "../prjbd2021/src/main/webapp/assets/json/"
data = datetime.today().strftime('%Y-%m-%d')

monitores = Produtos()
for pagina in range(1,6):
    monitores.iniciarDriver()
    monitores.obterLinksDeProdutos(
        "https://www.kabum.com.br/computadores/monitores?page_number="
            +str(pagina)
            +"&page_size=20&facet_filters=&sort=most_searched",
        "body > div > main > article > section > div > div > main > div > a",
        'Lamentamos, nenhum produto encontrado com esse critério de pesquisa.',
        '#listingEmpty > b')

    monitores.obterProdutos(2, 2, 'Ops! A página não foi encontrada.', '#__next > header~div span:first-child')
    monitores.liberarDriver()

monitores.criarArquivo(caminhoJson + "kabum-monitores-" + data + ".jl")