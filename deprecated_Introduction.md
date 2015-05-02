## Annotation ##

When navigating to the **Annotation** page, a dialogue opens that allows you to select a project, and a document within the project. If you want to open a different project or document later, click on **Open** button to open the dialog.

<img src='https://webanno.googlecode.com/svn/wiki/images/annotation1.jpg' width='800' />

Projects appear as folders, and contain the documents of the project. Double-click on a document to open it for annotation. Document names written in black show that the document has not been opened by the current user, blue font means that it has already been opened, whereas
red font indicates that the document has already been marked as "done".
Once the document is opened, a default of 10 sentences are loaded on the annotation  page.  The  **Settings**  button will  allow  you  to specify the settings of you the annotation layer.

The first option allows you to select an Annotation Layer you are displayed during annotation. The second option allows you to specify the number of sentences that will be displayed on one page. The last option allows you to select autoscroll while annotating. Autoscroll scrolls automatically forward by putting the last annotated sentence in the middle.

### Annotation Navigation ###



Sentence  numbers  on  the  left  side  of  the  annotation  page  shows  the  exact sentence numbers in the document.

<img src='https://webanno.googlecode.com/svn/wiki/images/annotation3.jpg' width='400' />

The arrow buttons **first page**, **next page**, **previous page**, **last page**, and **go to page** allow you to navigate accordingly. The **Prev.** and **Next** buttons in the **Document** frame allow you to go to the previous or next document on your project list. You can also use the following keyboard assignments in order to navigate only using your keyboard.
  * **HOME**: jump to first sentence
  * **END**: jump to last sentence
  * **PAGE DOWN**: move to the next page, if not in the last page already
  * **PAGE UP**: move to previous page , if not already in the first page
  * **SHIFT+PgUp** and **SHIFT+PgDn**: go to previous/next document in project, if available

Annotations are always immediately persistent in the backend database. Thus, it is not necessary to  save the annotations explicitly. Also, losing the connection through network issues or timeouts does not cause data loss. To obtain a local copy of the current document, click on **export** button . The document will be saved in TCF to your local disk, and can be re-imported via adding the document to a project by a project administrator. Please export your data periodically, at least when finishing a document or not continuing annotations for an extended period of time.
A click on the “Help” Button displays the Guidelines for the tool and “The Annotator's Guide to NER-Annotation”.
When you are finished with annotating or curating a document, please click on the “Done”
button      , so that the document may be further processed. If the button above the “Done” is a cross symbol, it means the documents has already been finished. If the symbol has a tick, it is still open.


<img src='https://webanno.googlecode.com/svn/wiki/images/annotation4.jpg' width='600' />

Annotation of spans works by selecting the span, or double-clicking on a word. This opens the annotation dialog, where you can add a new annotation. The layer can be selected on the left side of the frame. The Tag can be selected out of the right box, containing the tags of the tagset. One can also type in the initial letters and chose the needed tag. In the case of lemma annotation, a lemma can be typed into the box on the right.

<img src='https://webanno.googlecode.com/svn/wiki/images/annotation_edit.JPG' width='400' />

To change or delete an annotation, double-click on the annotation (span or link annotations).


Link annotations (between POS tags) are created by selecting the starting POS-tag, then dragging the arrow to connect it to its target POS tag. All possible targets are highlighted.


<img src='https://webanno.googlecode.com/svn/wiki/images/anno_pos_span.JPG' width='600' />


### Annotation Layers ###
Concerning annotation, WebAnno supports six layers:
Span annotations support NE, lemma, POS, and co-referance. Several annotation layers may be selected.

For Example, for NE annotation, select the options as shown below (red check mark):

<img src='https://webanno.googlecode.com/svn/wiki/images/annotation2.jpg' width='400' />

[NE](http://en.wikipedia.org/wiki/Named-entity_recognition) annotation can be chosen from a tagset and can span over several tokens within one sentence. Nested NE annotations are also possible (in the example below: "Frankfurter" in "Frankfurter FC").

<img src='https://webanno.googlecode.com/svn/wiki/images/annotation_ner.JPG' width='400' />

[Lemma](http://en.wikipedia.org/wiki/Lemma_%28morphology%29) annotation, as shown below, is freely selectable over a single token.

<img src='https://webanno.googlecode.com/svn/wiki/images/annotation_lemma.JPG' width='400' />


[POS](http://en.wikipedia.org/wiki/Part_of_speech) can be chosen over one token out of a tagset.

<img src='https://webanno.googlecode.com/svn/wiki/images/annotation_pos.JPG' width='400' />


[Co-reference](http://en.wikipedia.org/wiki/Coreference) annotation can be made over several tokens within one sentence. A single token sequence have several co-ref spans parallely.

<a href='Hidden comment: 
Make examples
'></a>

The other kind of annotation that is supported by WebAnno is Arc. The arcs can be made within one sentence and their types are chosen out of a tagset. Dependency arcs are defined between POS. Every POS is allowed to have only one head. Co-reference arcs are defined between co-reference-anchors. All transitivly combined anchors build a colour-coded lexical chain.

<img src='https://webanno.googlecode.com/svn/wiki/images/anno_span_many.JPG' width='400' />
