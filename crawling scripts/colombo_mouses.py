from datetime import datetime
from produtos import Produtos

caminhoJson = "../prjbd2021/src/main/webapp/assets/json/"
data = datetime.today().strftime('%Y-%m-%d')

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

mouses.criarArquivo(caminhoJson + "colombo-mouses-" + data + ".jl")