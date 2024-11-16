package AC2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Tabuleiro extends JFrame {
    private JPanel painel;
    private JPanel menu;
    private JButton iniciarButton;
    private JButton resetButton;
    private JButton pauseButton;
    private JButton modoButton; //// Botão para selecionar o modo de operação
    private JTextField placarField;
    private String direcao = "direita";
    private long tempoAtualizacao = 25;
    private int incremento = 2;
    private Quadrado obstaculo, cobra;
    private int larguraTabuleiro, alturaTabuleiro;
    private int placar = 0;
    private boolean modoColisao = true; //// Modo de colisão padrão: verdadeiro
    private ListaCircularSnakeGame listaCobra; //// declara um atributo chamado listaCobra, utilizando os métodos da classe ListaCircularSnakeGame
    private boolean jogoIniciado = false; //// Variável para verificar se o jogo está em andamento
    private boolean jogoPausado = false; //// Variável para controlar se o jogo está pausado
    private Color corFundo = new Color(127, 158, 1); //// Atributo para a cor de fundo

    public Tabuleiro() {
        larguraTabuleiro = alturaTabuleiro = 700;
        cobra = new Quadrado(10, 10, Color.black);
        cobra.x = larguraTabuleiro / 2;
        cobra.y = alturaTabuleiro / 2;
        obstaculo = new Quadrado(10, 10, Color.red);
        obstaculo.x = gerarNovaPosicaoX(); //// Gera uma posição aleatória para o obstáculo em X
        obstaculo.y = gerarNovaPosicaoY(); //// Gera uma posição aleatória para o obstáculo em Y
        setTitle("Snake Game");
        setSize(alturaTabuleiro, larguraTabuleiro + 30);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        menu = new JPanel();
        menu.setLayout(new FlowLayout());
        placarField = new JTextField("Score: 0");
        iniciarButton = new JButton("Iniciar");
        resetButton = new JButton("Reiniciar");
        pauseButton = new JButton("Pausar");
        modoButton = new JButton("Modo colisão: Padrão"); //// Inicializa o modo do botão como padão
        listaCobra = new ListaCircularSnakeGame(larguraTabuleiro / 2, alturaTabuleiro / 2); //// Cria a lista circular com a posição inicial
        this.setPreferredSize(new Dimension(700, 700)); //// Define o tamanho do tabuleiro

        placarField.setEditable(false);
        placarField.setEditable(false);
        menu.add(placarField);
        menu.add(iniciarButton);
        menu.add(resetButton);
        menu.add(pauseButton);
        menu.add(modoButton); //// Adiciona o botão de modo ao painel

        painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(corFundo); //// Define a cor de fundo
                g.fillRect(0, 0, getWidth(), getHeight()); //// Preenche o fundo com a cor

                //// Desenha cada segmento da cobra
                nodeSnake segmento = listaCobra.getHead();
                do {
                    g.setColor(Color.BLACK);
                    g.fillRect(segmento.getX(), segmento.getY(), 10, 10);
                    segmento = segmento.getNext();
                } while (segmento != listaCobra.getHead()); // Continua até retornar ao head

                // Desenha o obstáculo
                g.setColor(obstaculo.cor);
                g.fillRect(obstaculo.x, obstaculo.y, obstaculo.largura, obstaculo.altura);
            }
        };

        add(menu, BorderLayout.NORTH);
        add(painel, BorderLayout.CENTER);
        setVisible(true);

        // ActionListener para o botão Iniciar
        iniciarButton.addActionListener(e -> {
            Iniciar();
            painel.requestFocusInWindow(); // Devolve o foco para o painel
        });
        // ActionListener para o botão Reset
        resetButton.addActionListener(e -> {
            Reiniciar();
        });
        // ActionListener para o botão Pausar
        pauseButton.addActionListener(e -> {
            Pausar();
        });
        //// ActionListener para o botão Modo
        modoButton.addActionListener(e -> {
            modoColisao = !modoColisao; // Alterna o modo de colisão
            // Atualiza o texto do botão com base no modo atual
            modoButton.setText(modoColisao ? "Modo colisão: Padrão" : "Modo colisão: Ressurgir");
        });

        painel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (!direcao.equals("direita")) {
                            direcao = "esquerda";
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!direcao.equals("esquerda")) {
                            direcao = "direita";
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (!direcao.equals("baixo")) {
                            direcao = "cima";
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!direcao.equals("cima")) {
                            direcao = "baixo";
                        }
                        break;
                }
            }
        });
        painel.setFocusable(true);
        painel.requestFocusInWindow();
    }

    public int gerarNovaPosicaoX() { //// Método para gerar uma posição aleatória para o obstáculo na coordenada X
        return (int) (Math.random() * (larguraTabuleiro - obstaculo.largura));
    }

    public int gerarNovaPosicaoY() { //// Método para gerar uma posição aleatória para o obstáculo na coordenada Y
        return (int) (Math.random() * (alturaTabuleiro - obstaculo.altura));
    }

    private void Iniciar() {
        if (!jogoIniciado) {
            jogoIniciado = true;
            new Thread(() -> {
                while (true) {
                    if (!jogoPausado) { //// Verifica se o jogo não está pausado
                        try {
                            Thread.sleep(tempoAtualizacao);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //// Calcula as novas posições da cabeça da cobra em X e Y
                        int novaX = listaCobra.getHead().getX();
                        int novaY = listaCobra.getHead().getY();

                        switch (direcao) {
                            case "esquerda":
                                novaX -= incremento;
                                break;
                            case "direita":
                                novaX += incremento;
                                break;
                            case "cima":
                                novaY -= incremento;
                                break;
                            case "baixo":
                                novaY += incremento;
                                break;
                        }

                        //// Move a cobra com as novas coordenadas
                        listaCobra.moverCobra(novaX, novaY);

                        //// Verifica as colisões com o obstáculo
                        verificarColisaoObstaculo();
                        //// verifica as colisões com as bordas do tabuleiro
                        verificarBordaTabuleiro();
                        //// verifica a autocolisão com o corpo da cobra
                        verificarAutoColisao();

                        // Redesenha o painel
                        painel.repaint();
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    private void verificarAutoColisao() { //// Método para verificar auto colisão com a cobra
        nodeSnake body = listaCobra.getHead().getNext(); // Começa a verificar a partir do segundo segmento

        // Percorre todo o corpo da cobra
        while (body != listaCobra.getHead()) {
            // Verifica se a cabeça está na mesma posição que o segmento atual
            if (listaCobra.getHead().getX() == body.getX() && listaCobra.getHead().getY() == body.getY()) {
                // Se houver autocolisão, exibe a mensagem de "Game Over"
                JOptionPane.showMessageDialog(this, "Você colidiu com a cobra! Fim de jogo.", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0); // Encerra o jogo
                return;
            }
            body = body.getNext(); // Move para o próximo segmento do corpo
        }
    }

    private void verificarColisaoObstaculo() { //// Método para verificar se a cobra colidiu com o obstáculo
        int cobraAltura = 10; // Tamanho da altura da cobra
        int cobraLargura = 10; // Tamanho da largura da cobra

        // Coordenadas da cabeça da cobra em X e Y
        int cobraX = listaCobra.getHead().getX();
        int cobraY = listaCobra.getHead().getY();

        // Coordenadas e dimensões do obstáculo
        int obstaculoX = obstaculo.x; // Posição X do obstáculo
        int obstaculoY = obstaculo.y; // Posição Y do obstáculo
        int obstaculoLargura = obstaculo.largura; // Largura do obstáculo
        int obstaculoAltura = obstaculo.altura; // Altura do obstáculo

        // Verifica se o centro da cabeça da cobra está dentro do obstáculo
        boolean colisao = cobraX + cobraLargura > obstaculoX &&
                // cobraX + cobraLargura: calcula a posição X do lado direito da cabeça da cobra
                // obstaculoX: é a posição X do canto esquerdo do obstáculo
                cobraX < obstaculoX + obstaculoLargura && // verifica se o lado esquerdo da cabeça da cobra não
                                                          // ultrapassa o lado direito do obstáculo
                // cobraX: posição X do lado esquerdo da cabeça da cobra
                // obstaculoX + obstaculoLargura: calcula a posição X do lado direito do
                // obstáculo
                cobraY + cobraAltura > obstaculoY && // verifica se o lado inferior da cobra passou pelo lado superior do obstáculo
                // cobraY: é a posição Y do topo da cabeça da cobra
                // cobraAltura: é a altura da cabeça da cobra.
                // cobraY + cobraAltura: calcula a posição Y do lado inferior da cabeça da cobra
                cobraY < obstaculoY + obstaculoAltura; // verifica se o lado superior da cabeça da cobra não ultrapassa 
                                                      //o lado inferior do obstáculo.
                 // cobraY: é a posição do topo da cabeça da cobra
                 // obstaculoY + obstaculoAltura: calcula a posição Y do lado inferior do obstáculo

                //  Obs.: Se todas as quatro condições forem verdadeiras,
                //  significa que a área da cabeça da cobra se sobrepõe à área do obstáculo, indicando uma colisão!

        if (colisao) { // Se uma colisão for detectada
            // Incrementa o placar
            placar++;
            placarField.setText("Placar: " + placar); // Atualiza o texto do placar na interface gráfica
            tempoAtualizacao--; //Aumenta a velocidade da cobra a cada colisão com a maça
            listaCobra.adicionarSegmento(4); // Adiciona um novo quadrado à cabeça

            // Gera uma nova posição para o obstáculo nas posições X e Y
            obstaculo.x = gerarNovaPosicaoX();
            obstaculo.y = gerarNovaPosicaoY();
        }
    }

    private void Reiniciar() { //// Método para reiniciar o jogo
        jogoIniciado = false; // Encerra o loop do jogo atual
        jogoPausado = false; // Garante que o jogo não esteja pausado
        tempoAtualizacao = 35; // Restaura a velocidade original da cobra

        // Reinicia as variáveis e estado do jogo
        direcao = "direita";
        placar = 0;
        placarField.setText("Score: 0");

        // Reinicializa a cobra e o obstáculo
        listaCobra = new ListaCircularSnakeGame(larguraTabuleiro / 2, alturaTabuleiro / 2); // Reinicia a cobra
        cobra.x = larguraTabuleiro / 2; // Redefine a posição da cobra em X
        cobra.y = alturaTabuleiro / 2; // Redefine a posição da cobra em Y
        obstaculo.x = gerarNovaPosicaoX(); // Gera nova posição para o obstáculo em X
        obstaculo.y = gerarNovaPosicaoY(); // Gera nova posição para o obstáculo em Y

        painel.repaint(); // Redesenha o painel

        painel.requestFocusInWindow(); // Garante que o painel receba o foco novamente
    }

    private void Pausar() { //// Método para pausar o jogo
        if (jogoIniciado) {
            jogoPausado = !jogoPausado; // Alterna o estado de pausa

            if (jogoPausado) { // Se o jogo estiver pausado
                pauseButton.setText("Despausar"); // Opção de despausar
                JOptionPane.showMessageDialog(this, "Jogo Pausado!", "Pausar", JOptionPane.INFORMATION_MESSAGE);
            } else { // Se não estiver pausado
                pauseButton.setText("Pausar"); // opção de pausar
                JOptionPane.showMessageDialog(this, "Jogo Despausado!", "Pausar", JOptionPane.INFORMATION_MESSAGE);
            }
            painel.requestFocusInWindow(); // Garante que o painel receba o foco
        }
    }

    private void verificarBordaTabuleiro() { //// Método para verificar se a cabeça da cobra atingiu as bordas do tabuleiro
        nodeSnake head = listaCobra.getHead(); // Obtém a cabeça da cobra a partir da ListaCircularSnakeGame

        // Verifica se a cabeça da cobra está fora dos limites do tabuleiro
        if (head.getX() < 0 || head.getX() >= larguraTabuleiro || head.getY() < 0 || head.getY() >= alturaTabuleiro) {

            // Se o modo de colisão está ativado, exibe uma mensagem de "Game Over" e
            // encerra o jogo
            if (modoColisao) {
                JOptionPane.showMessageDialog(this, "Você colidiu com a borda! Fim de jogo.", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0); // Encerra o jogo
            } else {
                // Caso o modo de ressurgimento esteja ativado, a cobra ressurgirá do outro lado
                // do tabuleiro
                // Se a cabeça está em X à esquerda do tabuleiro, move para a borda direita
                if (head.getX() < 0) {
                    head.setX(larguraTabuleiro - 10); // Coloca a cabeça no lado direito do tabuleiro
                }
                // Se a cabeça está à direita do tabuleiro, move para a borda esquerda
                else if (head.getX() >= larguraTabuleiro) {
                    head.setX(0); // Coloca a cabeça no lado esquerdo do tabuleiro
                }

                // Se a cabeça está em Y acima do tabuleiro, move para a borda inferior
                if (head.getY() < 0) {
                    head.setY(alturaTabuleiro - 10); // Coloca a cabeça na parte inferior do tabuleiro
                }
                // Se a cabeça está em Y abaixo do tabuleiro, move para a borda superior
                else if (head.getY() >= alturaTabuleiro) {
                    head.setY(0); // Coloca a cabeça na parte superior do tabuleiro
                }
            }
        }
    }

    //// Método para permitir a configuração da cor de fundo
    public void setCorFundo(int r, int g, int b) {
        corFundo = new Color(r, g, b);
        painel.repaint(); // Reapresenta o painel para atualizar a cor
    }

    public static void main(String[] args) {
        new Tabuleiro();
    }
}


