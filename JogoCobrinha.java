import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.*;

public class JogoCobrinha extends JPanel implements ActionListener {

    private final int LARGURA_TELA = 600;
    private final int ALTURA_TELA = 600;
    private final int TAMANHO_BLOCO = 25;
    private final int UNIDADES = (LARGURA_TELA * ALTURA_TELA) / (TAMANHO_BLOCO * TAMANHO_BLOCO);
    private final int DELAY = 150; 

    private final int x[] = new int[UNIDADES];
    private final int y[] = new int[UNIDADES];
    private final Color coresCorpo[] = new Color[UNIDADES];

    private int corpoCobrinha;
    private int macasComidas;
    private int macaX;
    private int macaY;
    private Color corMacaAtual;
    private char direcao = 'R';
    private boolean rodando = false;
    private boolean fimDeJogo = false;
    
    private Timer timer;
    private Random random;
    private JButton botaoAcao;

    public JogoCobrinha() {
        random = new Random();
        this.setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        this.setBackground(Color.decode("#10e76d"));
        this.setFocusable(true);
        this.setLayout(null); // Permite posicionar o botão manualmente
        this.addKeyListener(new LeitorTeclas());

        // Configuração do Botão
        botaoAcao = new JButton("START GAME");
        botaoAcao.setBounds(LARGURA_TELA/2 - 100, ALTURA_TELA/2 - 25, 200, 50);
        botaoAcao.setFont(new Font("Arial", Font.BOLD, 20));
        botaoAcao.setFocusable(false); // Importante para não roubar o foco do teclado
        botaoAcao.addActionListener(e -> {
            botaoAcao.setVisible(false);
            iniciarOuResetarJogo();
        });
        
        this.add(botaoAcao);
    }

    public void iniciarOuResetarJogo() {
        corpoCobrinha = 6;
        macasComidas = 0;
        direcao = 'R';
        fimDeJogo = false;
        rodando = true;

        // Resetar posições
        for(int i = 0; i < corpoCobrinha; i++) {
            x[i] = 100 - (i * TAMANHO_BLOCO);
            y[i] = 100;
            coresCorpo[i] = Color.green;
        }

        novaMaca();
        
        if (timer != null) timer.stop();
        timer = new Timer(DELAY, this);
        timer.start();
        this.requestFocusInWindow(); // Garante que o teclado funcione após clicar
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenhar(g);
    }

    public void desenhar(Graphics g) {
        if (rodando) {
            // Maçã
            g.setColor(corMacaAtual);
            g.fillOval(macaX, macaY, TAMANHO_BLOCO, TAMANHO_BLOCO);

            // Cobrinha
            for (int i = 0; i < corpoCobrinha; i++) {
                if (i == 0) {
                    g.setColor(Color.white);
                } else {
                    g.setColor(coresCorpo[i] != null ? coresCorpo[i] : Color.green);
                }
                g.fillRect(x[i], y[i], TAMANHO_BLOCO, TAMANHO_BLOCO);
            }

            // Placar
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            g.drawString("Score: " + macasComidas, 10, 30);
        } else if (fimDeJogo) {
            telaFimDeJogo(g);
        } else {
            // Tela Inicial
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 50));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Snake Colorida", (LARGURA_TELA - metrics.stringWidth("Snake Colorida"))/2, ALTURA_TELA/3);
        }
    }

    public void novaMaca() {
        macaX = random.nextInt((int) (LARGURA_TELA / TAMANHO_BLOCO)) * TAMANHO_BLOCO;
        macaY = random.nextInt((int) (ALTURA_TELA / TAMANHO_BLOCO)) * TAMANHO_BLOCO;
        corMacaAtual = new Color(random.nextInt(155)+100, random.nextInt(155)+100, random.nextInt(155)+100);
    }

    public void mover() {
        for (int i = corpoCobrinha; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
            coresCorpo[i] = coresCorpo[i-1];
        }

        switch (direcao) {
            case 'U' -> y[0] -= TAMANHO_BLOCO;
            case 'D' -> y[0] += TAMANHO_BLOCO;
            case 'L' -> x[0] -= TAMANHO_BLOCO;
            case 'R' -> x[0] += TAMANHO_BLOCO;
        }
    }

    public void verificarMaca() {
        if ((x[0] == macaX) && (y[0] == macaY)) {
            coresCorpo[0] = corMacaAtual;
            corpoCobrinha++;
            macasComidas++;
            novaMaca();
        }
    }

    public void verificarColisoes() {
        for (int i = corpoCobrinha; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                rodando = false;
            }
        }
        if (x[0] < 0 || x[0] >= LARGURA_TELA || y[0] < 0 || y[0] >= ALTURA_TELA) {
            rodando = false;
        }
        if (!rodando) {
            timer.stop();
            fimDeJogo = true;
            botaoAcao.setText("RESTART");
            botaoAcao.setVisible(true);
        }
    }

    public void telaFimDeJogo(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics m1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (LARGURA_TELA - m1.stringWidth("Game Over")) / 2, ALTURA_TELA / 3);

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics m2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + macasComidas, (LARGURA_TELA - m2.stringWidth("Score: " + macasComidas)) / 2, g.getFont().getSize() + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (rodando) {
            mover();
            verificarMaca();
            verificarColisoes();
        }
        repaint();
    }

    public class LeitorTeclas extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> { if (direcao != 'R') direcao = 'L'; }
                case KeyEvent.VK_RIGHT -> { if (direcao != 'L') direcao = 'R'; }
                case KeyEvent.VK_UP -> { if (direcao != 'D') direcao = 'U'; }
                case KeyEvent.VK_DOWN -> { if (direcao != 'U') direcao = 'D'; }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake com Botão");
        JogoCobrinha jogo = new JogoCobrinha();
        frame.add(jogo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}