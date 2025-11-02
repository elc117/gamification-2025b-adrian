# Identificação 🪪:
Nome:  Adrian Bellé Secretti <br>
Curso: Sistemas de Informação

# Proposta 📄:
Este é um jogo web/desktop feito na biblioteca LibGDX em Java.
Se trata de um quiz com perguntas relacionadas a temas de computação. <br>
Para cada questão errada, o jogador perde vida.
Se chegar ao final sem morrer, vence o jogo e o inimigo, que faz as perguntas, é derrotado. <br>
O objetivo do projeto é criar uma forma divertida de praticar conhecimentos de programação.

# Processo de desenvolvimento 🛠️:
## Detalhes:
31/10: <br>
Comecei implementando o personagem principal. Criei a classe Knight, que é um cavaleiro
desenhado com um animation sprite. <br>
Baixei as imagens do site gameart2d.com. <br>
Depois, troquei o nome da classe Knight para Character, para poder reaproveitá-la. <br>
Então, criei o personagem wizard, que será o personagem que fará as perguntas. <br>
![image](images/10-31.png)

01/11: <br>
Criei 13 questões sobre computação e salvei em um arquivo .json para utilizar quando for implementá-las. <br>

02/11: <br>
Corrigi a textura do mago que estava invertida e fora do lugar. <br>
Configurei o makefile para por padrão testar a versão desktop, que é mais rápida para compilar. <br>

## Dificultades:
Já no início tive dificuldade em fazer o setup do projeto, pois estava dando erros de dependências. Resolvi editando os arquivos do gradle. <br>
Demorei para entender como o LibGDX funciona, pois nunca havia mexido com ele antes. <br>

# Diagrama de classes 📋:
TODO

# Orientações para execução 🖥️:
É necessário ter o Java JDK instalado.
```shell
cd game && ./gradlew html:superDev
```

# Resultado final 📊:
TODO

# Referências e créditos 📚:
LibGDX. <https://libgdx.com/>
Game Art 2D. THE KNIGHT - FREE SPRITES. <https://www.gameart2d.com/the-knight-free-sprites.html>
Raizensoft. How to create animations from spritesheets in libGDX. <https://raizensoft.com/tutorial/create-animation-spritesheet-libgdx/>
LibGDX. 2D Animation. <https://libgdx.com/wiki/graphics/2d/2d-animation>
itch.io. Wizard Pack by LuizMelo. <https://luizmelo.itch.io/wizard-pack>