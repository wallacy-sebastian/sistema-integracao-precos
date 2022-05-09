from datetime import datetime
from produtos import Produtos

caminhoJson = "../prjbd2021/src/main/webapp/assets/json/"
data = datetime.today().strftime('%Y-%m-%d')

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

monitores.criarArquivo(caminhoJson + "colombo-monitores-" + data + ".jl")