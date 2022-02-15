from produtos import Produtos

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

mouses.criarArquivo("./kabum_mouses.jl")

monitores = Produtos()
for pagina in range(1,6):
    monitores.obterLinksDeProdutos(
        "https://www.kabum.com.br/computadores/monitores?page_number="
            +str(pagina)
            +"&page_size=20&facet_filters=&sort=most_searched",
        "body > div > main > article > section > div > div > main > div > a",
        'Lamentamos, nenhum produto encontrado com esse critério de pesquisa.',
        '#listingEmpty > b')

monitores.obterProdutos(2, 2, 'Ops! A página não foi encontrada.', '#__next > header~div span:first-child')
monitores.criarArquivo("./kabum_monitores.jl")
monitores.liberarDriver()