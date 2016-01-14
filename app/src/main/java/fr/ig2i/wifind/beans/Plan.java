package fr.ig2i.wifind.beans;

import fr.ig2i.wifind.annotations.Serializable;

/**
 * Created by Thomas on 13/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class Plan {

    /**
     * Image associée au plan
     */
    @Serializable
    private Image image;

    /**
     * Obtenir l'image
     * @return
     */
    public Image getImage() {
        return image;
    }

    /**
     * Modifier l'image
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "image=" + image +
                '}';
    }
}
