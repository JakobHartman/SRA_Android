package org.rbdc.sra.objects;

import java.io.Serializable;

/**
 * Created by imac on 2/3/15.
 */
public class ImageData implements Serializable {
    String imageData;
    String url;
    String creationDate;
    String takenBy;

    public String getCreationDate() {
        return creationDate;
    }

    public String getImageData() {
        return imageData;
    }

    public String getUrl() {
        return url;
    }

    public String getTakenBy() {
        return takenBy;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
