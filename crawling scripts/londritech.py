from produtos import Produtos

mouses = Produtos()
for pagina in range(1,6):
    mouses.iniciarDriver()
    mouses.obterLinksDeProdutos(
        
        "https://londritech.com.br/b?cn=PERIFÉRICOS/MOUSE&cid=5036/5074/&pg="+str(pagina),
        "div.product-main section article header > a:first-child",
        'Voltar para home',
        'div.product-main > div > a')

    mouses.obterProdutos(3, 1, 'Voltar para home', 'div.product-main > div > a')
    mouses.liberarDriver()

mouses.criarArquivo("./londritech_mouses.jl")

monitores = Produtos()
for pagina in range(1,6):
    monitores.iniciarDriver()
    monitores.obterLinksDeProdutos(
        
        "https://londritech.com.br/b?cn=MONITOR/MONITOR&cid=5039/5092/&pg="+str(pagina),
        "div.product-main section article header > a:first-child",
        'Voltar para home',
        'div.product-main > div > a')

    monitores.obterProdutos(3, 2, 'Voltar para home', 'div.product-main > div > a')
    monitores.liberarDriver()

monitores.criarArquivo("./londritech_monitores.jl")