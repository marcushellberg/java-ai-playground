package org.vaadin.marcus.semantickernel.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.semantickernel.data.vectorstorage.attributes.VectorStoreRecordDataAttribute;
import com.microsoft.semantickernel.data.vectorstorage.attributes.VectorStoreRecordKeyAttribute;
import com.microsoft.semantickernel.data.vectorstorage.attributes.VectorStoreRecordVectorAttribute;
import com.microsoft.semantickernel.data.vectorstorage.definition.DistanceFunction;

import java.util.Collections;
import java.util.List;

public class TermsAndConditions {

    @VectorStoreRecordKeyAttribute
    private String id;
    @VectorStoreRecordDataAttribute
    private String content;
    @VectorStoreRecordVectorAttribute(dimensions = 1536, indexKind = "Hnsw", distanceFunction = DistanceFunction.COSINE_DISTANCE)
    private List<Float> contentVector;

    public TermsAndConditions() {
        this(null, null, Collections.emptyList());
    }

    public TermsAndConditions(@JsonProperty String id, @JsonProperty String content,
                  @JsonProperty List<Float> contentVector) {
        this.content = content;
        this.id = id;
        this.contentVector = contentVector;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Float> getContentVector() {
        return contentVector;
    }

    public void setContentVector(List<Float> contentVector) {
        this.contentVector = contentVector;
    }
}
