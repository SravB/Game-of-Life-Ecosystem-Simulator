/*
 * Unit 3 Cellular Automation Assignment
 * Ecosystem Modeling - Land and Sea
 * By: Sourav Biswas
 *  
 */

package gameoflife;

//imports
import java.io.*;
import java.util.Scanner;
import java.awt.*; 
import java.awt.image.BufferedImage;
import javax.swing.*; 
import static javax.swing.JFrame.EXIT_ON_CLOSE; 

public class GameOfLife extends JFrame {

    //generation variables
    int numGenerations = 10000;
    int currGeneration = 1;
    
    //colors
    Color LandColor = Color.decode("#993300");
    Color PlantColor = Color.decode("#1aff1a");
    Color PreyColor = Color.decode("#003300");
    Color PredatorColor = Color.RED;
    Color GridColor = Color.BLACK;
    
    Color WaterColor = Color.BLUE;
    Color WaterPlantColor = Color.decode("#99ff66");
    Color WaterPreyColor = Color.decode("#ff9999");
    Color WaterPredColor = Color.decode("#800080");
    
    
    //file reading variable
    String fileName = "Initial cells.txt";

    //window variables
    int width = 940; 
    int height = 970;
    int borderWidth = 50; 
    int sideborderWidth = 20;

    int numCellsX = 100; 
    int numCellsY = 100;
    
    /*array of integers
    0 - land
    1 - plant
    2 - prey
    3 - predator
    4 - water
    5 - water plant
    6 - water prey
    7 - water pred
    */
    int alive[][] = new int[numCellsY][numCellsX]; 
    int aliveNext[][] = new int[numCellsY][numCellsX]; 
    
    int cellWidth = (width - sideborderWidth * 2) / numCellsX; 
     
    //visual adjustments for labels
    int labelX = width / 2 - 50;
    int labelY = borderWidth - 5; 
 
    
    //METHODS
    
    //plants the first generation
    public void plantFirstGeneration() throws IOException {
        
        makeEveryoneDead();
                        
        for (int i = 0; i < numCellsY; i++) {
            for (int j = 0; j < numCellsX; j++) {
                
                //giving cells a 10% chance to start as a plant
                //or else they start as land or water
                double chance = Math.random() * 10;
                double chance2 = Math.random() * 10;
                
                if (chance <= 0.01) {
                    
                    //70% of planet is water - simulates realistic environment
                    if (chance2 <= 6) {
                        alive[i][j] = 5;
                        
                    } else {
                        alive[i][j] = 1;
                    }

                } else {
                    
                    //70% of planet is water - simulates realistic environment
                    if (chance2 <= 6) {
                        alive[i][j] = 4;
                        
                    } else {
                        alive[i][j] = 0;
                    }
                    
                }
                
            }
        }
    }

    
    //Sets all cells to dead (barren land)
    public void makeEveryoneDead() {
        
        for (int i = 0; i < numCellsY; i++) {
            for (int j = 0; j < numCellsX; j++) {
                alive[i][j] = 4;
            }
        }
    }

    
    //reads the first generations' alive cells from a file
    public void plantFromFile(String fileName) throws IOException {

        //file reading variables
        FileReader f = new FileReader(fileName);
        Scanner s = new Scanner(f);

        int x, y, type;
        
        //reading file
        while ( s.hasNext() ) {
            
            x = s.nextInt();
            y = s.nextInt();
            type = s.nextInt();
            alive[y][x] = type;
            
        }
    }

    
    //Applies the rules of The Game of Life to set the values of 
    //the aliveNext[][] array,
    //based on the current values in the alive[][] array
    public void computeNextGeneration() {
                
        for (int i = 0; i < numCellsY; i++) {
            for (int j = 0; j < numCellsX; j++) {
                
                
                //water
                if (alive[i][j] > 3) {
                    
                    //if anything living is completely surrounded by land, it dies
                    if (alive[i][j] != 4 && countLivingNeighbors(j,i)[5] + countLivingNeighbors(j,i)[6] + countLivingNeighbors(j,i)[7] == 0) {
                        aliveNext[i][j] = 4;
                    
                    //all other cases    
                    } else {
                        
                        if (alive[i][j] == 4) {         //water
                            double chance = Math.random() * 100;

                            if (countLivingNeighbors(j,i)[1] + countLivingNeighbors(j,i)[5] >= 3 || chance <= 1) {
                                aliveNext[i][j] = 5;

                            } else if (countLivingNeighbors(j,i)[0] >= 4) {
                                aliveNext[i][j] = 0;
                                
                            } else {
                                aliveNext[i][j] = 4;
                            }

                        } else if(alive[i][j] == 5) {      //water plant

                            if (countLivingNeighbors(j,i)[6] > 2 || countLivingNeighbors(j,i)[5] == 8){
                                aliveNext[i][j] = 6;

                            } else if (countLivingNeighbors(j,i)[7] > 2) {
                                aliveNext[i][j] = 7;
                                
                            } else if (countLivingNeighbors(j,i)[1] + countLivingNeighbors(j,i)[5] == 0) {
                                aliveNext[i][j] = 4;
                                
                            } else {
                                aliveNext[i][j] = 5;
                            }

                        } else if(alive[i][j] == 6) {      //water prey

                            if (countLivingNeighbors(j,i)[7] >= 1) {
                                aliveNext[i][j] = 7;
                            
                            } else if (countLivingNeighbors(j,i)[5] == 0) {
                                aliveNext[i][j] = 5;
                                
                            } else if (countLivingNeighbors(j,i)[6] >= 4) {

                                //10% chance for evolution into predator
                                double chance = Math.random() * 10;

                                // evolution into predator
                                if (chance <= 1) {
                                    aliveNext[i][j] = 7; 

                                } else {
                                    aliveNext[i][j] = 4;
                                }

                            } else {
                                aliveNext[i][j] = 6;
                            }

                        } else {       //water pred
                            
                            if (countLivingNeighbors(j,i)[7] > 2 || countLivingNeighbors(j,i)[6] == 0) {
                                aliveNext[i][j] = 4;

                            } else {
                                aliveNext[i][j] = 7;
                            }
                        }
                    }
                    
                    
                   
                //land    
                } else {
                    
                    //if anything living is completely surrounded by land, it dies
                    if (alive[i][j] != 0 && countLivingNeighbors(j,i)[1] + countLivingNeighbors(j,i)[2] + countLivingNeighbors(j,i)[3] == 0) {
                        aliveNext[i][j] = 0;
                    
                    //all other cases    
                    } else {
                    
                        //if block is land
                        if (alive[i][j] == 0) {
                        
                            //1% chance to evolve land into plant
                            double chance = Math.random() * 100;
                        
                            if (countLivingNeighbors(j,i)[1] + countLivingNeighbors(j,i)[5] >= 3 || chance <= 1) {
                                aliveNext[i][j] = 1;
                            
                            } else if (countLivingNeighbors(j,i)[4] >= 5) {
                                aliveNext[i][j] = 4;
                                
                            } else {
                                aliveNext[i][j] = 0;
                            }
                  
                        //if block is a plant    
                        } else if (alive[i][j] == 1) {
                        
                            if (countLivingNeighbors(j,i)[3] >= 3) { 
                                aliveNext[i][j] = 3;
                            
                            } else if (countLivingNeighbors(j,i)[2] >= 2 || countLivingNeighbors(j,i)[1] == 8){
                                aliveNext[i][j] = 2;
                            
                            } else if (countLivingNeighbors(j,i)[1] + countLivingNeighbors(j,i)[5] == 0){
                                aliveNext[i][j] = 0;
                            
                            } else {
                                aliveNext[i][j] = 1;
                            }
                
                        //if block is a prey
                        } else if (alive[i][j] == 2) {

                            if (countLivingNeighbors(j,i)[3] >= 1) { 
                                aliveNext[i][j] = 3;

                            } else if (countLivingNeighbors(j,i)[1] == 0) { 
                                aliveNext[i][j] = 0;

                            } else if (countLivingNeighbors(j,i)[2] >= 4) {

                                //10% chance for evolution into predator
                                double chance = Math.random() * 10;

                                // evolution into predator
                                if (chance <= 1) {
                                    aliveNext[i][j] = 3; 

                                } else {
                                    aliveNext[i][j] = 1;
                                }

                            } else {
                                aliveNext[i][j] = 2;
                            }

                        //if block is predator
                        } else {

                            if (countLivingNeighbors(j,i)[2] == 0) {
                                aliveNext[i][j] = 0;

                            } else if (countLivingNeighbors(j,i)[3] >= 3) {
                                aliveNext[i][j] = 1;

                            } else {
                                aliveNext[i][j] = 3;

                            }
                        }
                    } 
                }
            }
        }
    }

    
    //Overwrites the current generation's 2-D array with the values from the 
    //next generation's 2-D array
    public void plantNextGeneration() {
                
        for (int i = 0; i < numCellsY; i++) {
            for (int j = 0; j < numCellsX; j++) {
                alive[i][j] = aliveNext[i][j];
            }
        }
    }

    
    //Counts the number of cells adjacent to cell (i, j) 
    //returns array of number of neighbours sorted by type
    public int[] countLivingNeighbors(int i, int j) {
        
         //initializing variables
         int start = 0; 
         int stop = 3;
         int[] neighbours = new int[8];
         
         //finds start and stop values
         if (i == 0) {
             start++;
             
         } else if (i == numCellsX - 1) {
             stop--;
         } 
         
         //finding neighbours
         for(int n = start; n < stop; n++) {
             
             if (j > 0) {
                neighbours[alive[j - 1][i - 1 + n]]++;
             }

             if (n != 1) {
                neighbours[alive[j][i - 1 + n]]++;
             }
             
             if (j < numCellsY - 1) {
                neighbours[alive[j + 1][i - 1 + n]]++;
             }
         }
         
         //adjustment for edge cells
         //pretends border is land
         int s = neighbours[0] + neighbours[1] + neighbours[2] + neighbours[3] + neighbours[4] + neighbours[5] + neighbours[6] + neighbours[7];
         
         if (s < 8) {
             neighbours[4] += 8 - s;
         }
         
         return neighbours; 
         
    }

    
    //Makes the pause between generations
    public static void sleep(int duration) {
        
        try {
            Thread.sleep(duration);
        } 
        
        catch (Exception e) {}
        
    }

    
    //Displays the statistics at the top of the screen
    void drawLabel(Graphics g, int state) {
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.drawString("Generation: " + state, labelX, labelY);
        
    }
    
    
    //Draws the current generation of living cells on the screen
    public void paint( Graphics g ) {
        
        Image img = createImage();
        g.drawImage(img, 0, 0, this);
        
    }


    //creates image for paint method
    private Image createImage() {
        
        //image and labels
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        
        int x, y, i, j;
        
        drawLabel(g, currGeneration);
        
        //coloring in cells
        for (i = 0; i < numCellsY; i++) {
            
            y = borderWidth + cellWidth * i;  
            
            for (j = 0; j < numCellsX; j++) {
                
                x = sideborderWidth + cellWidth * j;
                
                if (alive[i][j] == 0) {
                    g.setColor(LandColor);
                    
                } else if (alive[i][j] == 1) {
                    g.setColor(PlantColor);
                    
                } else if (alive[i][j] == 2) {
                    g.setColor(PreyColor);
                    
                } else if (alive[i][j] == 3) {
                    g.setColor(PredatorColor);
                    
                } else if (alive[i][j] == 4) {
                    g.setColor(WaterColor);
                    
                } else if (alive[i][j] == 5) {
                    g.setColor(WaterPlantColor);
                    
                } else if (alive[i][j] == 6) {
                    g.setColor(WaterPreyColor);
                    
                } else {
                    g.setColor(WaterPredColor);
                }
                
                g.fillRect(x, y, cellWidth, cellWidth);
                
            }   
        }
        
        //draws gridlines
        //draws horizontal lines
        g.setColor(GridColor);
        for (i = 0; i < numCellsY + 1; i++) { 
            g.drawLine(sideborderWidth, borderWidth + cellWidth * i, width - sideborderWidth, 
                    borderWidth + cellWidth * i);
        } 
        
        //draws vertical lines
        for (i = 0; i < numCellsX + 1; i++) { 
            g.drawLine(sideborderWidth + cellWidth * i, borderWidth, sideborderWidth + cellWidth * i, 
                    height - sideborderWidth);
        }
        
        return bufferedImage;
        
    }
    
    
    //Sets up the JFrame screen
    public void initializeWindow() {
        
        setTitle("Game of Life Simulator");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //calls paint() for the first time
        setVisible(true); 
        
    }
    
    
    //Main algorithm
    public static void main(String args[]) throws IOException {

        GameOfLife currGame = new GameOfLife();
        
        //Sets the initial generation of living cells, either by reading from a 
        //file or creating them algorithmically
        currGame.plantFirstGeneration(); 
        currGame.initializeWindow();
        
        //main loop
        for (int i = 1; i <= currGame.numGenerations; i++) {
            
            
            currGame.currGeneration = i;
            currGame.repaint(); 
            
            //pauses for 100 milliseconds
            sleep(100); 
            
            //////////////////////////////////////////////////////////////////////
            currGame.computeNextGeneration();
            currGame.plantNextGeneration();
            
        }
    } 
} //end of class
