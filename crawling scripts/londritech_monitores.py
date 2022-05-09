from datetime import datetime
from produtos import Produtos

caminhoJson = "../prjbd2021/src/main/webapp/assets/json/"
data = datetime.today().strftime('%Y-%m-%d')

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

monitores.criarArquivo(caminhoJson + "londritech-monitores-" + data + ".jl")