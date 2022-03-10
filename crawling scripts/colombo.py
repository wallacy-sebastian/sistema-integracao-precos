from produtos import Produtos

mouses = Produtos()
for pagina in range(1,6):
    mouses.iniciarDriver()
    mouses.obterLinksDeProdutos(
        
        "https://www.colombo.com.br/produto/Informatica/Acessorios-e-Perifericos/Mouse?ordem=3&pagina="+str(pagina),
        ".produto-imagem-content .click-product-analytics",
        'Voltar para home',
        'div.product-main > div > a')

    mouses.obterProdutos(1, 1, 'Voltar para home', 'div.product-main > div > a')
    mouses.liberarDriver()

mouses.criarArquivo("./colombo_mouses.jl")

monitores = Produtos()
for pagina in range(1,6):
    monitores.iniciarDriver()
    monitores.obterLinksDeProdutos(
        
        "https://www.colombo.com.br/produto/Informatica/Monitores?ordem=3&pagina="+str(pagina),
        ".produto-imagem-content .click-product-analytics",
        'Voltar para home',
        'div.product-main > div > a')

    monitores.obterProdutos(1, 2, 'Voltar para home', 'div.product-main > div > a')
    monitores.liberarDriver()

monitores.criarArquivo("./colombo_monitores.jl")