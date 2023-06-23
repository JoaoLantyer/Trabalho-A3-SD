	# Projeto A3 de Sistemas Distribuídos

    Para utilizar o aplicativo você precisa ter a versão mais recente do Java SE Development Kit (JDK) instalada em seu computador e utilizar o sistema operacional Windows.

1.	Baixe o arquivo .zip do repositório e descompacte.
2.	Caso o arquivo “database.db” não exista, execute “startServidor.bat” uma vez. Isso irá abrir o processo “Servidor” e criar o banco de dados com os dados necessários.
3.	Com o arquivo “database.db” disponível na pasta, execute “startProcessos.bat”. Isso irá abrir 5 processos diferentes, um processo “Servidor”, dois processos “Gerente” e dois processos “Vendedor”.
4.	Feito isso o aplicativo estará pronto para o uso.
5.	Para testar o comportamento caso o servidor seja derrubado, feche o processo do servidor (o processo com o identificador 5).
6.	Agora, ao tentar realizar alguma solicitação, a eleição será chamada e o processo com maior identificador assumirá as funcionalidades do servidor (líder).
7.	Para fazer com que o servidor retorne e retome suas funcionalidades, execute o arquivo “startServidor.bat” e realize alguma solicitação com algum dos clientes.
8.	Feito isso uma eleição será chamada e o servidor se tornará líder novamente e retomará suas funções (já que tem o maior identificador).
