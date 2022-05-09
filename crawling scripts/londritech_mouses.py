from datetime import datetime
from produtos import Produtos

caminhoJson = "../prjbd2021/src/main/webapp/assets/json/"
data = datetime.today().strftime('%Y-%m-%d')

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

mouses.criarArquivo(caminhoJson + "londritech-mouses-" + data + ".jl")