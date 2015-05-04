/*
 * Copyright 2015 Kovacs Peter Tibor
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package chiliad.parser.pdf.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class MPage implements Serializable {

    private static final long serialVersionUID = 625306993560294227L;
    private String sourceId;
    private Integer pageNumber;
    private Double width;
    private Double height;
    private List<MImage> images = new LinkedList<>();
    private List<MToken> tokens = new LinkedList<>();
    private MVectorGraphics vectorGraphics = null;

    public static MPage newInstance(String sourceId, Integer pageNumber, Double width, Double height) {
        MPage p = new MPage();
        p.pageNumber = pageNumber;
        p.width = width;
        p.height = height;
        return p;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void add(MImage image) {
        images.add(image);
    }

    public void add(MToken token) {
        tokens.add(token);
    }

    public void addTokens(List<MToken> tokens) {
        this.tokens.addAll(tokens);
    }

    public void addImages(List<MImage> images) {
        this.images.addAll(images);
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<MImage> getImages() {
        return images;
    }

    public void setImages(List<MImage> images) {
        this.images = images;
    }

    public List<MToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<MToken> tokens) {
        this.tokens = tokens;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public MVectorGraphics getVectorGraphics() {
        return vectorGraphics;
    }

    public void setVectorGraphics(MVectorGraphics vectorGraphics) {
        this.vectorGraphics = vectorGraphics;
    }

}
