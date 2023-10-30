package uniandes.dpoo.taller4.view;

import  uniandes.dpoo.taller4.modelo.RegistroTop10.*;
import  uniandes.dpoo.taller4.modelo.Top10.*;
import  uniandes.dpoo.taller4.modelo.Tablero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import com.formdev.flatlaf.FlatLightLaf;

public class View extends JFrame {
    private static final long serialVersionUID = -5113883391557041387L;
	private JPanel tableroPanel;
    private int filas = 5;
    private int columnas = 5;
    private Casilla[][] casillas;
    private Tablero juego;
    private Top10 top10;
    private File top10File;


    public View() {
        setTitle("Tablero de Juego");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        juego = new Tablero(filas);
        juego.desordenar(10);

        tableroPanel = new JPanel(new GridLayout(filas, columnas));
        casillas = new Casilla[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                casillas[i][j] = new Casilla(i, j);
                tableroPanel.add(casillas[i][j]);
            }
        }

        add(tableroPanel, BorderLayout.CENTER);

        JButton reiniciarButton = new JButton("Reiniciar");
        reiniciarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                juego.reiniciar();
                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        casillas[i][j].actualizarEstado(juego.darTablero()[i][j]);
                        casillas[i][j].resetContador();
                    }
                }
            }
            
        });

        JPanel opcionesPanel = new JPanel();
        opcionesPanel.add(reiniciarButton);
        add(opcionesPanel, BorderLayout.SOUTH);
        
        JButton top10Button = new JButton("Top-10");
        top10Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarTop10();
            }
        });
        
        JButton cambiarJugadorButton = new JButton("Cambiar Jugador");
        cambiarJugadorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        JPanel opcionesPanel = new JPanel();
        opcionesPanel.add(reiniciarButton);
        opcionesPanel.add(top10Button); 
        opcionesPanel.add(cambiarJugadorButton); 
        add(opcionesPanel, BorderLayout.SOUTH);

        setSize(500, 500);
        
        top10 = new Top10();
        top10File = new File("top10.txt");
        top10.cargarRecords(top10File);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                salvarTop10();
            }
        });
        
    }

    private class Casilla extends JPanel {
    	
        private static final long serialVersionUID = -474200895511260167L;
        
		private int fila;
		
        private int columna;
        
        private int contador = 0;
        
        private Color color = Color.WHITE;
        
            

        public Casilla(int fila, int columna) {
        	
            this.fila = fila;
            this.columna = columna;
            setOpaque(true);
            setLayout(new BorderLayout());
            actualizarEstado(juego.darTablero()[fila][columna]);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	contador++;
                    actualizarEstado(juego.darTablero()[fila][columna]);
                    juego.jugar(fila, columna);
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                	if (juego.darTablero()[fila][columna]) {
                		casillas[fila][columna].actualizarEstado(false);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                	if (juego.darTablero()[fila][columna]) {
                		casillas[fila][columna].actualizarEstado(true);
                    } 
                   
                    if (contador > 0) {
                        repaint();
                    }
                }

          
                });
        }

        public void actualizarEstado(boolean encendida) {
            if (encendida) {
            	color = Color.YELLOW;
            } else {
            	color = Color.BLACK;
            }
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(0, 0, color, getWidth(), getHeight(), Color.LIGHT_GRAY);
            g2d.setPaint(gradient);
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

            if (contador > 0) {
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 18));
                g2d.drawString(Integer.toString(contador), getWidth() / 2 - 10, getHeight() / 2 + 7);
            }
        }
        
        public void resetContador() {
            contador = 0;
            actualizarEstado(juego.darTablero()[fila][columna]);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                View ventana = new View();
                ventana.setVisible(true);
            }
        });
    }
}