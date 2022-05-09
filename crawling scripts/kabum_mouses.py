from datetime import datetime
from produtos import Produtos

caminhoJson = "../prjbd2021/src/main/webapp/assets/json/"
data = datetime.today().strftime('%Y-%m-%d')

mouses = Produtos()
for pagina in range(1,6):
    mouses.iniciarDriver()
    mouses.obterLinksDeProdutos(
        "https://www.kabum.com.br/perifericos/teclado-mouse/mouse?page_number="
            +str(pagina)
            +"&page_size=20&facet_filters=&sort=most_searched",
        "body > div > main > article > section > div > div > main > div > a",
        'Lamentamos, nenhum produto encontrado com esse critério de pesquisa.',
        '#listingEmpty > b')

    mouses.obterProdutos(2, 1, 'Ops! A página não foi encontrada.', '#__next > header~div span:first-child')
    mouses.liberarDriver()

mouses.criarArquivo(caminhoJson + "kabum-mouses-" + data + ".jl")