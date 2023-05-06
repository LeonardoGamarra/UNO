/**
 * This code plays a game of UNO
 * @author Leonardo Gamarra
 * @version 1.1
 */

import java.util.Scanner;
import java.util.ArrayList;

public class UNO
{
    public static void main(String[] args) throws InterruptedException
    {
        Scanner in = new Scanner(System.in);

        // Declares arrays used to store cards that have already been picked
        int[][] fullDeck = new int[13][4];
        int[] extraDeck = new int[2];

        // Declares and initializes arrays used to store locations of cards
        String[][] fullDeckString = new String[13][4];
        String[] extraDeckString = {"Multicolor", "+4"};

        for (int i = 0; i < 13; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                fullDeckString[i][j] = determineCard(i, j);
            }
        }

        // Declares players decks
        ArrayList<String> playerDeck = new ArrayList<String>();
        ArrayList<String> cpuDeck = new ArrayList<String>();

        // Determines the first player to receive cards
        int cardReceiver = (int) Math.floor(Math.random() * 2);

        // Creates variables used to deal cards
        String card;
        int deckType = 0;
        int typeLocation = 0;
        int colorLocation = 0;
        int totalUsedCards = 0;
        String lastCard = "";
        boolean cardFound = false;
        boolean validCard = true;

        System.out.println("Bem-vindo ao UNO!");
        Thread.sleep(1500);
        System.out.println("As cartas serão distribuídas!");
        System.out.println();

        // Uses a loop to deal cards to players
        for (int i = 0; i < 15; i++)
        {
            do
            {
                // Generates a random card
                card = cardGenerator();
                cardFound = false;
                validCard = true;

                // Finds where the card is located
                for (int j = 0; j < 2; j++)
                {
                    if (extraDeckString[j].equals(card))
                    {
                        typeLocation = j;
                        deckType = 0;
                        cardFound = true;
                    }
                }
                for (int j = 0; j < 13; j++)
                {
                    if (cardFound)
                        break;
                    for (int k = 0; k < 4; k++)
                    {
                        if (fullDeckString[j][k].equals(card))
                        {
                            typeLocation = j;
                            colorLocation = k;
                            deckType = 1;
                            cardFound = true;
                        }
                    }
                }

                // Uses the information about where the card is to find out if it has not been picked yet
                if (deckType == 0 && extraDeck[typeLocation] < 4)
                {
                    extraDeck[typeLocation]++;
                }
                else if (deckType == 1 && typeLocation == 0 && fullDeck[typeLocation][colorLocation] < 1)
                {
                    fullDeck[typeLocation][colorLocation]++;
                }
                else if (deckType == 1 && typeLocation != 0 && fullDeck[typeLocation][colorLocation] < 2)
                {
                    fullDeck[typeLocation][colorLocation]++;
                }
                else
                {
                    // If it has already been picked, generates another card
                    validCard = false;
                }
                if (validCard)
                {
                    totalUsedCards++;
                }
            }
            while (!validCard);

            if (i == 14)
            {
                lastCard = card;
                if (lastCard.equals("+4") || lastCard.equals("Multicolor"))
                {
                    lastCard += " " + randomColor();
                }
            }
            else if (cardReceiver == 0)
            {
                playerDeck.add(card);
                Thread.sleep(1500);
                System.out.println("Você recebeu " + checkColor(card) + card + "\u001B[0m");
                cardReceiver = 1;
            }
            else if (cardReceiver == 1)
            {
                cpuDeck.add(card);
                cardReceiver = 0;
            }
        }
        System.out.println();

        // Main game loop
        String playingCard = "";
        String legacyPlayingCard = "";
        boolean extraCard = false;
        boolean validPlayingCard = false;
        boolean playerHasCard = false;
        int round = 0;
        int counter = 0;
        int nowPlaying = (int) Math.floor(Math.random() * 2);
        do
        {
            // Decides who will have the turn
            round++;
            System.out.println("JOGADA " + round);
            Thread.sleep(1500);
            System.out.println();
            if (!lastCard.contains("Bloqueio") && !lastCard.contains("+4"))
            {
                if (nowPlaying == 0)
                {
                    nowPlaying = 1;
                }
                else if (nowPlaying == 1)
                {
                    nowPlaying = 0;
                }
            }

            // Display last card and player deck
            if (nowPlaying == 0)
            {
                System.out.println("A última carta jogada foi: " + checkColor(lastCard) + lastCard + "\u001B[0m");
                System.out.println("Seu total de cartas: " + playerDeck.size() + " // " + "Total de cartas do adversário: " + cpuDeck.size());
                System.out.println();
                if (playerDeck.size() == 1 || cpuDeck.size() == 1)
                {
                    System.out.println("\u001B[1mUNO!\u001B[0m");
                }
                System.out.print("Suas cartas: ");
                playerDeck = organizePlayerDeck(playerDeck);
                for (int i = 0; i < playerDeck.size(); i++)
                {
                    System.out.print(checkColor(playerDeck.get(i)) + playerDeck.get(i) + "\u001B[0m");
                    if (i != (playerDeck.size() - 1))
                    {
                        System.out.print("|");
                    }
                    else
                    {
                        System.out.println();
                    }
                }
                System.out.println();
            }

            validPlayingCard = false;
            playerHasCard = false;
            playingCard = "";

            // Player`s turn
            if (nowPlaying == 0)
            {
                do
                {
                    Thread.sleep(1500);
                    System.out.print("É a sua vez de jogar digite o nome da carta ou (comprar): ");
                    playingCard = in.nextLine();
                    System.out.println();

                    // Allows player to buy cards
                    if (playingCard.equals("comprar"))
                    {
                        do
                        {
                            // Checks if there are remaining cards
                            if (totalUsedCards == 108)
                            {
                                totalUsedCards = 0;
                                fullDeck = resetFullDeck(fullDeck);
                                extraDeck = resetExtraDeck(extraDeck);
                                Thread.sleep(1500);
                                System.out.println();
                                System.out.println("As cartas acabaram, reiniciando baralho!");
                                System.out.println();
                            }
                            // Generates a random card
                            card = cardGenerator();
                            cardFound = false;
                            validCard = true;
                            validPlayingCard = false;
                            playerHasCard = false;

                            // Finds where the card is located
                            for (int j = 0; j < 2; j++)
                            {
                                if (extraDeckString[j].equals(card))
                                {
                                    typeLocation = j;
                                    deckType = 0;
                                    cardFound = true;
                                }
                            }
                            for (int j = 0; j < 13; j++)
                            {
                                if (cardFound)
                                    break;
                                for (int k = 0; k < 4; k++)
                                {
                                    if (fullDeckString[j][k].equals(card))
                                    {
                                        typeLocation = j;
                                        colorLocation = k;
                                        deckType = 1;
                                        cardFound = true;
                                    }
                                }
                            }

                            // Uses the information about where the card is to find out if it has not been picked yet
                            if (deckType == 0 && extraDeck[typeLocation] < 4)
                            {
                                extraDeck[typeLocation]++;
                            }
                            else if (deckType == 1 && typeLocation == 0 && fullDeck[typeLocation][colorLocation] < 1)
                            {
                                fullDeck[typeLocation][colorLocation]++;
                            }
                            else if (deckType == 1 && typeLocation != 0 && fullDeck[typeLocation][colorLocation] < 2)
                            {
                                fullDeck[typeLocation][colorLocation]++;
                            }
                            else
                            {
                                // If it has already been picked, generates another card
                                validCard = false;
                            }

                            if (validCard)
                            {
                                // Updates total used cards value
                                totalUsedCards++;

                                // Adds the card to player deck
                                playerDeck.add(card);
                                Thread.sleep(1500);
                                System.out.println("Você comprou " + checkColor(card) + card + "\u001B[0m");

                                // Checks if player`s new card is valid
                                if (deckType == 0)
                                {
                                    validPlayingCard = true;
                                    extraCard = true;
                                }
                                else if (lastCard.contains(card.substring(0, card.indexOf(" "))) || lastCard.contains(card.substring((card.indexOf(" ") + 1))))
                                {
                                    validPlayingCard = true;
                                }
                                if (validPlayingCard)
                                {
                                    playingCard = card;
                                    playerHasCard = true;
                                }
                            }
                        }
                        while (!validCard || !validPlayingCard);
                        System.out.println();
                    }
                    else
                    {
                        // Checks if player has chosen card

                        for (int i = 0; i < playerDeck.size(); i++)
                        {
                            if (playingCard.equals(playerDeck.get(i)) || extraCard)
                            {
                                playerHasCard = true;
                                break;
                            }
                        }
                        if (!playerHasCard)
                        {
                            System.out.println("Você não possui esta carta!");
                        }
                        extraCard = false;

                        // Checks if player`s card is valid
                        if (playingCard.equals("+4") || playingCard.equals("Multicolor"))
                        {
                            validPlayingCard = true;
                        }
                        else if (playingCard.contains(lastCard.substring(lastCard.indexOf(" "))) || playingCard.contains(lastCard.substring(0, lastCard.indexOf(" "))))
                        {
                            validPlayingCard = true;
                        }
                    }
                }
                while (!validPlayingCard || !playerHasCard);

                // Asks player what color they want to choose
                String userColor;
                if (playingCard.equals("+4") || playingCard.equals("Multicolor"))
                {
                    System.out.print("Suas cartas: ");
                    playerDeck = organizePlayerDeck(playerDeck);
                    for (int i = 0; i < playerDeck.size(); i++)
                    {
                        System.out.print(checkColor(playerDeck.get(i)) + playerDeck.get(i) + "\u001B[0m");
                        if (i != (playerDeck.size() - 1))
                        {
                            System.out.print("|");
                        }
                        else
                        {
                            System.out.println();
                        }
                    }
                    System.out.print("Qual cor você deseja (Amarelo, Azul, Verde, Vermelho): ");
                    legacyPlayingCard = playingCard;
                    do
                    {
                        userColor = " " + in.nextLine();
                        if (!userColor.contains("Amarelo") && !userColor.contains("Azul") && !userColor.contains("Verde") && !userColor.contains("Vermelho"))
                        {
                            System.out.println("Esta cor não pode ser escolhida!");
                        }
                    }
                    while (!userColor.contains("Amarelo") && !userColor.contains("Azul") && !userColor.contains("Verde") && !userColor.contains("Vermelho"));
                    playingCard += userColor;
                }
                Thread.sleep(1500);
                System.out.println("Você jogou " + checkColor(playingCard) + playingCard + "\u001B[0m");

                // Updates last card
                lastCard = playingCard;
                if (playingCard.contains("+4") || playingCard.contains("Multicolor"))
                {
                    playingCard = legacyPlayingCard;
                }

                // Removes used card from player`s deck
                playerDeck.remove(playingCard);
            }

            // Cpu`s turn
            else if (nowPlaying == 1)
            {
                for (int i = 0; i < cpuDeck.size(); i++)
                {
                    if (cpuDeck.get(i).contains(lastCard.substring(0, lastCard.indexOf(" "))) || cpuDeck.get(i).contains(lastCard.substring(lastCard.indexOf(" ") + 1)))
                    {
                        playingCard = cpuDeck.get(i);
                        break;
                    }
                }

                if (playingCard.equals(""))
                {
                    for (int i = 0; i < cpuDeck.size(); i++)
                    {
                        if (cpuDeck.get(i).equals("+4") || cpuDeck.get(i).equals("Multicolor"))
                        {
                            playingCard = cpuDeck.get(i);
                            break;
                        }
                    }
                }

                if (playingCard.equals(""))
                {
                    do
                    {
                        // Checks if there are remaining cards
                        if (totalUsedCards == 108)
                        {
                            totalUsedCards = 0;
                            fullDeck = resetFullDeck(fullDeck);
                            extraDeck = resetExtraDeck(extraDeck);
                            Thread.sleep(1500);
                            System.out.println("As cartas acabaram, reiniciando baralho");
                        }

                        // Generates a random card
                        card = cardGenerator();
                        cardFound = false;
                        validCard = true;

                        // Finds where the card is located
                        for (int j = 0; j < 2; j++)
                        {
                            if (extraDeckString[j].equals(card))
                            {
                                typeLocation = j;
                                deckType = 0;
                                cardFound = true;
                            }
                        }
                        for (int j = 0; j < 13; j++)
                        {
                            if (cardFound)
                                break;
                            for (int k = 0; k < 4; k++)
                            {
                                if (fullDeckString[j][k].equals(card))
                                {
                                    typeLocation = j;
                                    colorLocation = k;
                                    deckType = 1;
                                    cardFound = true;
                                }
                            }
                        }

                        // Uses the information about where the card is to find out if it has not been picked yet
                        if (deckType == 0 && extraDeck[typeLocation] < 4)
                        {
                            extraDeck[typeLocation]++;
                        }
                        else if (deckType == 1 && typeLocation == 0 && fullDeck[typeLocation][colorLocation] < 1)
                        {
                            fullDeck[typeLocation][colorLocation]++;
                        }
                        else if (deckType == 1 && typeLocation != 0 && fullDeck[typeLocation][colorLocation] < 2)
                        {
                            fullDeck[typeLocation][colorLocation]++;
                        }
                        else
                        {
                            // If it has already been picked, generates another card
                            validCard = false;
                        }

                        if (validCard)
                        {
                            // Updates total used cards value
                            totalUsedCards++;

                            // Adds the card to player deck
                            cpuDeck.add(card);
                            Thread.sleep(1500);
                            System.out.println("O adversário comprou uma carta");

                            // Checks if player`s new card is valid
                            if (deckType == 0)
                            {
                                validPlayingCard = true;
                            }
                            else if (lastCard.contains(card.substring(0, card.indexOf(" "))) || lastCard.contains(card.substring((card.indexOf(" ") + 1))))
                            {
                                validPlayingCard = true;
                            }
                            if (validPlayingCard)
                            {
                                playingCard = card;
                            }
                        }
                    }
                    while (!validCard || !validPlayingCard);
                }

                // Asks cpu for a color
                if (playingCard.equals("+4") || playingCard.equals("Multicolor"))
                {
                    legacyPlayingCard = playingCard;
                    for (int i = 0; i < cpuDeck.size(); i++)
                    {
                        if (cpuDeck.get(i).contains(" "))
                        {
                            playingCard += " " + cpuDeck.get(i).substring(cpuDeck.get(i).indexOf(" ") + 1);
                            break;
                        }
                        else if (i == (cpuDeck.size() - 1) && !playingCard.contains(" "))
                        {
                            playingCard += " " + randomColor();
                            break;
                        }
                    }
                }
                System.out.println("O adversário jogou " + checkColor(playingCard) + playingCard + "\u001B[0m");

                // Updates last card
                lastCard = playingCard;

                // Removes used card from cpu deck
                if (playingCard.contains("+4") || playingCard.contains("Multicolor"))
                {
                    playingCard = legacyPlayingCard;
                }
                cpuDeck.remove(playingCard);
            }

            if (playingCard.equals("+4") || playingCard.contains("+2"))
            {
                counter = 0;
                if (playingCard.contains("+2"))
                {
                    counter = 2;
                }
                while (counter < 4)
                {
                    do
                    {
                        // Checks if there are remaining cards
                        if (totalUsedCards == 108)
                        {
                            totalUsedCards = 0;
                            fullDeck = resetFullDeck(fullDeck);
                            extraDeck = resetExtraDeck(extraDeck);
                            Thread.sleep(1500);
                            System.out.println("As cartas acabaram, reiniciando baralho");
                        }

                        // Generates a random card
                        card = cardGenerator();
                        cardFound = false;
                        validCard = true;

                        // Finds where the card is located
                        for (int j = 0; j < 2; j++)
                        {
                            if (extraDeckString[j].equals(card))
                            {
                                typeLocation = j;
                                deckType = 0;
                                cardFound = true;
                            }
                        }
                        for (int j = 0; j < 13; j++)
                        {
                            if (cardFound)
                                break;
                            for (int k = 0; k < 4; k++)
                            {
                                if (fullDeckString[j][k].equals(card))
                                {
                                    typeLocation = j;
                                    colorLocation = k;
                                    deckType = 1;
                                    cardFound = true;
                                }
                            }
                        }

                        // Uses the information about where the card is to find out if it has not been picked yet
                        if (deckType == 0 && extraDeck[typeLocation] < 4)
                        {
                            extraDeck[typeLocation]++;
                        }
                        else if (deckType == 1 && typeLocation == 0 && fullDeck[typeLocation][colorLocation] < 1)
                        {
                            fullDeck[typeLocation][colorLocation]++;
                        }
                        else if (deckType == 1 && typeLocation != 0 && fullDeck[typeLocation][colorLocation] < 2)
                        {
                            fullDeck[typeLocation][colorLocation]++;
                        }
                        else
                        {
                            // If it has already been picked, generates another card
                            validCard = false;
                        }
                        if (validCard)
                        {
                            totalUsedCards++;
                        }
                    }
                    while (!validCard);

                    // Adds the card in the correct player deck
                    if (nowPlaying == 0)
                    {
                        cpuDeck.add(card);
                        Thread.sleep(1500);
                        System.out.println("O adversário comprou uma carta");
                    }
                    else if (nowPlaying == 1)
                    {
                        playerDeck.add(card);
                        Thread.sleep(1500);
                        System.out.println("Você comprou " + checkColor(card) + card + "\u001B[0m");
                    }
                    counter++;
                }
            }
            System.out.println();
        }
        while (playerDeck.size() != 0 && cpuDeck.size() != 0);
        if (playerDeck.size() == 0)
        {
            System.out.println("Parabéns, você venceu a partida de UNO! :)");
        }
        else
        {
            System.out.println("Você perdeu, mais sorte na próxima vez! :(");
        }
        in.close();
    }
    public static String cardGenerator()
    /**
     * This method is used to generate a new card
     * @author Leonardo Gamarra
     * @return It returns a new card
     */
    {
        // Generates two string arrays with possible card combinations
        String[] color = {"Amarelo", "Azul", "Verde", "Vermelho"};
        String[] type = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Bloqueio", "Reverso", "+2", "Multicolor", "+4"};

        // Generates two random number to pick a card
        int colorRandom = (int) Math.floor(Math.random() * 4);
        int typeRandom = (int) Math.floor(Math.random() * 15);

        // Creates a new card based on the random numbers generated
        String card;
        if (typeRandom < 13)
        {
            card = type[typeRandom] + " " + color[colorRandom];
        }
        else
        {
            card = type[typeRandom];
        }

        // Returns the generated card
        return card;
    }
    public static String determineCard(int i, int j)
    {
        String color = "";
        String type = "";

        if (j == 0)
            color = "Amarelo";
        else if (j == 1)
            color = "Azul";
        else if (j == 2)
            color = "Verde";
        else if (j == 3)
            color = "Vermelho";

        if (i < 10)
            type = Integer.toString(i);
        else if (i == 10)
            type = "Bloqueio";
        else if (i == 11)
            type = "Reverso";
        else if (i == 12)
            type = "+2";

        String card = type + " " + color;
        return card;
    }
    public static String randomColor()
    {
        String color = "";
        int i = (int) Math.ceil(Math.random() * 4);

        if (i == 1)
            color = "Amarelo";
        else if (i == 2)
            color = "Azul";
        else if (i == 3)
            color = "Verde";
        else
            color = "Vermelho";

        return color;
    }
    public static String checkColor(String card)
    {
        String color = "";
        if (card.contains(" "))
        {
            color = card.substring(card.indexOf(" ") + 1);
        }
        else if (card.equals("+4"))
        {
            color = "\u001B[35m";
        }
        else
        {
            color = "\u001B[36m";
        }
        if (color.equals("Amarelo"))
        {
            color = "\u001B[33m";
        }
        else if (color.equals("Azul"))
        {
            color = "\u001B[34m";
        }
        else if (color.equals("Verde"))
        {
            color = "\u001B[32m";
        }
        else if (color.equals("Vermelho"))
        {
            color = "\u001B[31m";
        }
        return color;
    }
    public static int[][] resetFullDeck(int[][] fullDeck)
    {
        for (int i = 0; i < 13; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                fullDeck[i][j] = 0;
            }
        }
        return fullDeck;
    }
    public static int[] resetExtraDeck(int[] extraDeck)
    {
        for (int i = 0; i < 2; i++)
        {
            extraDeck[i] = 0;
        }
        return extraDeck;
    }
    public static ArrayList<String> organizePlayerDeck(ArrayList<String> deck)
    {
        ArrayList<String> organizedDeck = new ArrayList<String>();

        // Organizes cards by colors

        for (int i = 0; i < 12; i++)
        {
            for (int j = 0; j < deck.size(); j++)
            {
                if (deck.get(j).contains("Amarelo") && deck.get(j).contains(Integer.toString(i)))
                {
                    organizedDeck.add(deck.get(j));
                }
                else if (deck.get(j).contains("Amarelo") && deck.get(j).contains("Bloqueio") && i == 10)
                {
                    organizedDeck.add(deck.get(j));
                }
                else if (deck.get(j).contains("Amarelo") && deck.get(j).contains("Reverso") && i == 11)
                {
                    organizedDeck.add(deck.get(j));
                }
            }
        }
        for (int i = 0; i < 12; i++)
        {
            for (int j = 0; j < deck.size(); j++)
            {
                if (deck.get(j).contains("Azul") && deck.get(j).contains(Integer.toString(i)) && i < 10)
                {
                    organizedDeck.add(deck.get(j));
                }
                else if (deck.get(j).contains("Azul") && deck.get(j).contains("Bloqueio") && i == 10)
                {
                    organizedDeck.add(deck.get(j));
                }
                else if (deck.get(j).contains("Azul") && deck.get(j).contains("Reverso") && i == 11)
                {
                    organizedDeck.add(deck.get(j));
                }
            }
        }
        for (int i = 0; i < 12; i++)
        {
            for (int j = 0; j < deck.size(); j++)
            {
                if (deck.get(j).contains("Verde") && deck.get(j).contains(Integer.toString(i)) && i < 10)
                {
                    organizedDeck.add(deck.get(j));
                }
                else if (deck.get(j).contains("Verde") && deck.get(j).contains("Bloqueio") && i == 10)
                {
                    organizedDeck.add(deck.get(j));
                }
                else if (deck.get(j).contains("Verde") && deck.get(j).contains("Reverso") && i == 11)
                {
                    organizedDeck.add(deck.get(j));
                }
            }
        }
        for (int i = 0; i < 12; i++)
        {
            for (int j = 0; j < deck.size(); j++)
            {
                if (deck.get(j).contains("Vermelho") && deck.get(j).contains(Integer.toString(i)) && i < 10)
                {
                    organizedDeck.add(deck.get(j));
                }
                else if (deck.get(j).contains("Vermelho") && deck.get(j).contains("Bloqueio") && i == 10)
                {
                    organizedDeck.add(deck.get(j));
                }
                else if (deck.get(j).contains("Vermelho") && deck.get(j).contains("Reverso") && i == 11)
                {
                    organizedDeck.add(deck.get(j));
                }
            }
        }
        for (int i = 0; i < deck.size(); i++)
        {
            if (deck.get(i).equals("Multicolor"))
            {
                organizedDeck.add(deck.get(i));
            }
        }
        for (int i = 0; i < deck.size(); i++)
        {
            if (deck.get(i).equals("+4"))
            {
                organizedDeck.add(deck.get(i));
            }
        }
        return organizedDeck;
    }
}