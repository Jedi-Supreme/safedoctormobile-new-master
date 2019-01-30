package com.safedoctor.safedoctor.Model;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class Document implements Serializable {
    private String condocument;
    private String documentmimetype;
    private String remarks;
    private Attachmenttype documenttype;


    public String getCondocument() {
        return condocument;
    }

    public void setCondocument(String condocument) {
        this.condocument = condocument;
    }

    public String getDocumentmimetype() {
        return documentmimetype;
    }

    public void setDocumentmimetype(String documentmimetype) {
        this.documentmimetype = documentmimetype;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Attachmenttype getDocumenttype() {
        return documenttype;
    }

    public void setDocumenttype(Attachmenttype documenttype) {
        this.documenttype = documenttype;
    }
}
