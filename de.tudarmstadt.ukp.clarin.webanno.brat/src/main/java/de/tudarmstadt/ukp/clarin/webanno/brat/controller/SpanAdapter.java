/*******************************************************************************
 * Copyright 2012
 * Ubiquitous Knowledge Processing (UKP) Lab and FG Language Technology
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.clarin.webanno.brat.controller;

import static java.util.Arrays.asList;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureStructureImpl;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.JCas;
import org.uimafit.util.CasUtil;

import de.tudarmstadt.ukp.clarin.webanno.brat.ApplicationUtils;
import de.tudarmstadt.ukp.clarin.webanno.brat.annotation.BratAnnotatorModel;
import de.tudarmstadt.ukp.clarin.webanno.brat.annotation.BratAnnotatorUIData;
import de.tudarmstadt.ukp.clarin.webanno.brat.display.model.Entity;
import de.tudarmstadt.ukp.clarin.webanno.brat.display.model.Offsets;
import de.tudarmstadt.ukp.clarin.webanno.brat.message.GetDocumentResponse;
import de.tudarmstadt.ukp.clarin.webanno.model.Mode;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * A class that is used to create Brat Span to CAS and vice-versa
 *
 * @author Seid Muhie Yimam
 * @author Richard Eckart de Castilho
 */
public class SpanAdapter
    implements TypeAdapter
{
    private Log LOG = LogFactory.getLog(getClass());
    /**
     * Prefix of the label value for Brat to make sure that different annotation types can use the
     * same label, e.g. a POS tag "N" and a named entity type "N".
     *
     * This is used to differentiate the different types in the brat annotation/visualization. The
     * prefix will not stored in the CAS(striped away at {@link BratAjaxCasController#getType} )
     */
    private String typePrefix;

    /**
     * A field that takes the name of the feature which should be set, e.e. "pos" or "lemma".
     */
    private String attachFeature;

    /**
     * A field that takes the name of the annotation to attach to, e.g.
     * "de.tudarmstadt...type.Token" (Token.class.getName())
     */
    private String attachType;

    /**
     * The UIMA type name.
     */
    private String annotationTypeName;

    /**
     * The feature of an UIMA annotation containing the label to be displayed in the UI.
     */
    private String labelFeatureName;

    private boolean singleTokenBehavior = false;

    public SpanAdapter(String aTypePrefix, String aTypeName, String aLabelFeatureName,
            String aAttachFeature, String aAttachType)
    {
        typePrefix = aTypePrefix;
        labelFeatureName = aLabelFeatureName;
        annotationTypeName = aTypeName;
        attachFeature = aAttachFeature;
        attachType = aAttachType;
    }

    /**
     * Span can only be made on a single token (not multiple tokens), e.g. for POS or Lemma
     * annotations. If this is set and a span is made across multiple tokens, then one annotation of
     * the specified type will be created for each token. If this is not set, a single annotation
     * covering all tokens is created.
     */
    public void setSingleTokenBehavior(boolean aSingleTokenBehavior)
    {
        singleTokenBehavior = aSingleTokenBehavior;
    }

    /**
     * @see #setSingleTokenBehavior(boolean)
     */
    public boolean isSingleTokenBehavior()
    {
        return singleTokenBehavior;
    }

    /**
     * Add annotations from the CAS, which is controlled by the window size, to the brat response
     * {@link GetDocumentResponse}
     *
     * @param aJcas
     *            The JCAS object containing annotations
     * @param aResponse
     *            A brat response containing annotations in brat protocol
     * @param aBratAnnotatorModel
     *            Data model for brat annotations
     */
    @Override
    public void render(JCas aJcas, GetDocumentResponse aResponse,
            BratAnnotatorModel aBratAnnotatorModel)
    {
        int address = BratAjaxCasUtil.selectSentenceAt(aJcas, aBratAnnotatorModel.getSentenceBeginOffset(), aBratAnnotatorModel.getSentenceEndOffset()).getAddress();
        // The first sentence address in the display window!
        Sentence firstSentence = (Sentence) BratAjaxCasUtil.selectByAddr(aJcas,
                FeatureStructure.class, address);
        int i = address;

        int lastSentenceAddress;
        if(aBratAnnotatorModel.getMode().equals(Mode.CURATION)){
            lastSentenceAddress = aBratAnnotatorModel.getLastSentenceAddress();
        }
        else{
            lastSentenceAddress = BratAjaxCasUtil.getLastSenetnceAddress(aJcas);
        }
        // Loop based on window size
        // j, controlling variable to display sentences based on window size
        // i, address of each sentences
        int j = 1;
        while (j <= aBratAnnotatorModel.getWindowSize()) {
            if (i >= lastSentenceAddress) {
                Sentence sentence = (Sentence) BratAjaxCasUtil.selectByAddr(aJcas,
                        FeatureStructure.class, i);
                updateResponse(sentence, aResponse, firstSentence.getBegin());
                break;
            }
            else {
                Sentence sentence = (Sentence) BratAjaxCasUtil.selectByAddr(aJcas,
                        FeatureStructure.class, i);
                updateResponse(sentence, aResponse, firstSentence.getBegin());
                i = BratAjaxCasUtil.getFollowingSentenceAddress(aJcas, i);
            }
            j++;
        }
    }

    /**
     * a helper method to the {@link #render(JCas, GetDocumentResponse, BratAnnotatorModel)}
     *
     * @param aSentence
     *            The current sentence in the CAS annotation, with annotations
     * @param aResponse
     *            The {@link GetDocumentResponse} object with the annotation from CAS annotation
     * @param aFirstSentenceOffset
     *            The first sentence offset. The actual offset in the brat visualization window will
     *            be <b>X-Y</b>, where <b>X</b> is the offset of the annotated span and <b>Y</b> is
     *            aFirstSentenceOffset
     */
    private void updateResponse(Sentence aSentence, GetDocumentResponse aResponse,
            int aFirstSentenceOffset)
    {
        Type type = CasUtil.getType(aSentence.getCAS(), annotationTypeName);
        for (AnnotationFS fs : CasUtil.selectCovered(type, aSentence)) {

            Feature labelFeature = fs.getType().getFeatureByBaseName(labelFeatureName);
            aResponse.addEntity(new Entity(((FeatureStructureImpl) fs).getAddress(), typePrefix
                    + fs.getStringValue(labelFeature), asList(new Offsets(fs.getBegin()
                    - aFirstSentenceOffset, fs.getEnd() - aFirstSentenceOffset))));
        }
    }

    /**
     * Update the CAS with new/modification of span annotations from brat
     *
     * @param aLabelValue
     *            the value of the annotation for the span
     */
    public void add(String aLabelValue, JCas aJcas, int aAnnotationOffsetStart,
            int aAnnotationOffsetEnd)
    {
        Map<Integer, Integer> offsets = ApplicationUtils.offsets(aJcas);

        if (singleTokenBehavior) {
            Map<Integer, Integer> splitedTokens = ApplicationUtils.getSplitedTokens(offsets,
                    aAnnotationOffsetStart, aAnnotationOffsetEnd);
            for (Integer start : splitedTokens.keySet()) {
                updateCas(aJcas.getCas(), start, splitedTokens.get(start), aLabelValue);
            }
        }
        else {
            int startAndEnd[] = ApplicationUtils.getTokenStart(offsets, aAnnotationOffsetStart,
                    aAnnotationOffsetEnd);
            updateCas(aJcas.getCas(), startAndEnd[0], startAndEnd[1], aLabelValue);
        }
    }

    /**
     * A Helper method to {@link #add(String, BratAnnotatorUIData)}
     */
    private void updateCas(CAS aCas, int aBegin, int aEnd, String aValue)
    {

        boolean duplicate = false;
        Type type = CasUtil.getType(aCas, annotationTypeName);
        Feature feature = type.getFeatureByBaseName(labelFeatureName);
        for (AnnotationFS fs : CasUtil.selectCovered(aCas, type, aBegin, aEnd)) {

            if (fs.getBegin() == aBegin && fs.getEnd() == aEnd) {
                if (!fs.getStringValue(feature).equals(aValue)) {
                    fs.setStringValue(feature, aValue);
                }
                duplicate = true;
            }
        }
        if (!duplicate) {
            AnnotationFS newAnnotation = aCas.createAnnotation(type, aBegin, aEnd);
            newAnnotation.setStringValue(feature, aValue);

            if (attachFeature != null) {
                Type theType = CasUtil.getType(aCas, attachType);
                Feature posFeature = theType.getFeatureByBaseName(attachFeature);
                CasUtil.selectCovered(aCas, theType, aBegin, aEnd).get(0)
                        .setFeatureValue(posFeature, newAnnotation);
            }
            aCas.addFsToIndexes(newAnnotation);
        }
    }

    /**
     * Delete a span annotation from CAS
     *
     * @param aJCas
     *            the CAS object
     * @param aId
     *            the low-level address of the span annotation.
     */
    public void delete(JCas aJCas, AnnotationFS aRefFs)
    {
        FeatureStructure fs = (FeatureStructure) BratAjaxCasUtil.selectByAddr(aJCas,
                FeatureStructure.class, ((FeatureStructureImpl) aRefFs).getAddress());
        aJCas.removeFsFromIndexes(fs);
    }

    /**
     * Convenience method to get an adapter for part-of-speech.
     *
     * NOTE: This is not meant to stay. It's just a convenience during refactoring!
     */
    public static final SpanAdapter getPosAdapter()
    {
        SpanAdapter adapter = new SpanAdapter(AnnotationTypeConstant.POS_PREFIX,
                POS.class.getName(), AnnotationTypeConstant.POS_FEATURENAME, "pos",
                Token.class.getName());
        adapter.setSingleTokenBehavior(true);
        return adapter;
    }

    /**
     * Convenience method to get an adapter for lemma.
     *
     * NOTE: This is not meant to stay. It's just a convenience during refactoring!
     */
    public static final SpanAdapter getLemmaAdapter()
    {
        SpanAdapter adapter = new SpanAdapter("", Lemma.class.getName(),
                AnnotationTypeConstant.LEMMA_FEATURENAME, "lemma", Token.class.getName());
        adapter.setSingleTokenBehavior(true);
        return adapter;
    }

    /**
     * Convenience method to get an adapter for named entity.
     *
     * NOTE: This is not meant to stay. It's just a convenience during refactoring!
     */
    public static final SpanAdapter getNamedEntityAdapter()
    {
        SpanAdapter adapter = new SpanAdapter(AnnotationTypeConstant.NAMEDENTITY_PREFIX,
                NamedEntity.class.getName(), AnnotationTypeConstant.NAMEDENTITY_FEATURENAME, null,
                null);
        adapter.setSingleTokenBehavior(false);
        return adapter;
    }

    @Override
    public String getLabelFeatureName()
    {
        // TODO Auto-generated method stub
        return labelFeatureName;
    }
}
