package br.edu.infnet.cli.modules;

import java.util.Scanner;

/**
 * Classe base com métodos comuns para todas as CLIs
 */
public abstract class BaseCli {
    
    protected Scanner scanner;
    
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
    
    protected void limparTela() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    protected void pausa() {
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        scanner.nextLine();
    }
}