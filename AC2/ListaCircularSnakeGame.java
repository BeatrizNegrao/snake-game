package AC2;

public class ListaCircularSnakeGame {
    private nodeSnake head; // Segmento da cabeça
    private nodeSnake tail; // Segmento da cauda
    private nodeSnake next;  // próximo segmento 

    public ListaCircularSnakeGame(int startX, int startY) {
        head = new nodeSnake(startX, startY); // Cria a cabeça da cobra com as coordenadas iniciais (startX, startY)
        tail = head; // cabeça é igual a cauda
        tail.setNext(head); // Liga o segmento (cauda) ao primeiro segmento (cabeça), formando uma lista circular
    }

   public void adicionarSegmento(int quantidadeSegmento) {
        for (int i = 0; i < quantidadeSegmento; i++) { //Para cada iteração, um novo segmento será adicionado à cobra.
            // Determina posição do novo segmento com base na posição da cauda nos eixos X e Y
            int novoSegmentoX = tail.getX();
            int novoSegmentoY = tail.getY();

            // Define posição do novo segmento conforme a direção do movimento
            // Verifica se a cobra está se movendo verticalmente
            if (head.getX() == tail.getX()) { // Se a coordenada X da cabeça for igual à da cauda, O movimento é vertical
                                              
                // Verifica se a cabeça está acima da cauda
                if (head.getY() < tail.getY()) { // Movimento para cima
                    novoSegmentoY += 10; // Aumenta a coordenada Y para posicionar o novo segmento acima 

                    // Se a cabeça está abaixo da cauda
                } else { // Movimento para baixo
                    novoSegmentoY -= 10; // Diminui a coordenada Y para posicionar o novo segmento abaixo
                }
                // Verifica se a cobra está se movendo horizontalmente
            } else if (head.getY() == tail.getY()) { // Se a coordenada Y da cabeça for igual à da cauda, movimento é horizontal
                if (head.getX() < tail.getX()) { // Movimento para a esquerda
                    novoSegmentoX += 10; // Aumenta a coordenada X para posicionar o novo segmento à esquerda
                } else { // Movimento para a direita
                    novoSegmentoX -= 10;  // Diminui a coordenada X para posicionar o novo segmento à direita
                }
            }

            // Cria o novo segmento na posição calculada
            nodeSnake novoSegmento = new nodeSnake(novoSegmentoX, novoSegmentoY);

            // as ligações da lista circular
            tail.setNext(novoSegmento); //a cauda aponta para o próximo segmento (novoSegmento)
            novoSegmento.setNext(head); // o novo segmento aponta para a cabeça da cobra
            tail = novoSegmento; // Atualiza a cauda para o novo segmento, tornando a nova cauda da cobra
        }
    }
    
    public void moverCobra(int novaX, int novaY) { // Método para mover a cobra de acordo com a nova posição em X e Y
        int tempX = head.getX(); // Armazena temporariamente a posição X da cabeça
        int tempY = head.getY(); // Armazena temporariamente a posição Y da cabeça
        head.setX(novaX); // a cabeça na posição X aponta para a nova posição em X
        head.setY(novaY); // a cabeça na posião Y aponta para a nova posição em Y

        nodeSnake current = head.getNext(); // atual é o próximo segmento da cabeça da cobra

        while (current != head) { /// enquanto o atual for diferente da cabeça
            int nextX = current.getX(); // Armazena a posição X do próximo segmento
            int nextY = current.getY(); // Armazena a posição Y do próximo segmento
            current.setX(tempX); // O segmento atual recebe a posição X do segmento anterior
            current.setY(tempY); // O segmento atual recebe a posição Y do segmento anterior
            tempX = nextX; // Atualiza a posição X temporária para a do próximo segmento
            tempY = nextY; // Atualiza a posição Y temporária para a do próximo segmento
            current = current.getNext(); // Move para o próximo segmento
        }
    }

    // Retorna a cabeça da cobra
    public nodeSnake getHead() {
        return head;
    }

    // Retorna a cauda da cobra
    public nodeSnake getTail() {
        return tail;
    }

    // Retorna o próximo segmento depois da cabeça da cobra
    public nodeSnake getNext() {
        return next;
    }
}
