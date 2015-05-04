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
package chiliad.parser.pdf.extractor.image;

import chiliad.parser.pdf.extractor.ExtractorException;
import chiliad.parser.pdf.extractor.PageExtractor;
import chiliad.parser.pdf.model.MImage;
import chiliad.parser.pdf.model.MPage;
import com.google.common.collect.ImmutableList;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFStreamEngine;
import org.apache.pdfbox.util.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageExtractor extends PDFStreamEngine implements PageExtractor {

    final static Logger LOG = LoggerFactory.getLogger(ImageExtractor.class);

    List<PDFImage> images = new ArrayList<>();
    private boolean onlyDimension = false;

    public ImageExtractor() throws IOException {
        super(ResourceLoader.loadProperties(
                "ImageExtractor.properties", true));
    }

    public boolean isOnlyDimension() {
        return onlyDimension;
    }

    public void setOnlyDimension(boolean onlyDimension) {
        this.onlyDimension = onlyDimension;
    }

    @Override
    public MPage extract(PDPage pageToExtract, MPage pageContent) {
        try {
            if (pageToExtract.getContents() == null) {
                throw new IllegalStateException("The PDPage content is null.");
            }
            processStream(pageToExtract, pageToExtract.findResources(), pageToExtract.getContents().getStream());
            pageContent.addImages(getMImages());
            return pageContent;
        } catch (IOException ex) {
            throw new ExtractorException("Failed to extract images.", ex);
        }

    }

    @Override
    public void reset() {
        super.resetEngine();
        images.clear();
    }

    /**
     * Operators can add the extracted image.
     *
     * @param pdfImage
     */
    public void addPDFImage(PDFImage pdfImage) {
        images.add(pdfImage);
    }

    private List<MImage> getMImages() {
        return ImmutableList.copyOf(images.stream().map(i -> toMImage(i)).collect(Collectors.toList()));
    }

    private MImage toMImage(PDFImage image) {
        MImage mImage = new MImage();
        mImage.setImageName(image.getImageName());
        mImage.setX((double) image.getX());
        mImage.setY((double) image.getY());
        mImage.setWidth(image.getWidth());
        mImage.setHeight(image.getHeight());
        if (!isOnlyDimension()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                image.getImage().write2OutputStream(baos);
            } catch (IOException ex) {
                ex.printStackTrace();;
            }
            mImage.setImageBytes(baos.toByteArray());
        }
        return mImage;
    }
}
