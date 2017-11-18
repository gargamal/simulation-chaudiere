/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chaudiere;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Nabot
 */
public class Extraction
{

  private static final String SEPARATEUR = ",";
  /*
   * Créer un fichier CSV avec en séparateur la ","
   */

  public void creerFichier(List<Acquisition> donnees, String nomFichier) throws IOException
  {
    Path path = Paths.get(nomFichier);
    try
    {
      Files.createFile(path);
    }
    catch (FileAlreadyExistsException ex) // Le fichier existe déjà
    {
      System.out.println("Le fichier " + nomFichier + " existe déjà.");
    }

    try (BufferedWriter flux = Files.newBufferedWriter(path, Charset.forName("UTF8")))
    {
      // Ecritrue des titres
      flux.write("L'intervalle d'acquistion est d'un point par seconde.\n");
      flux.write("T°C eau,T°C air,Conso (Kilo Joules)\n");

      StringBuilder strLigne = new StringBuilder();
      for (Acquisition ligne : donnees)
      {
        strLigne.delete(0, strLigne.length());
        strLigne.append(ligne.getTemperatureCelsusEau()).append(SEPARATEUR);
        strLigne.append(ligne.getTemperatureCelsusAir()).append(SEPARATEUR);
        strLigne.append(ligne.getEnergieConsommeeCumulee()).append("\n");
        flux.write(strLigne.toString());
      }
    }
  }
}
