# Automation #
This functionality gives the possibility to choose features and documents, which can be used for training of all layers that are offered in WebAnno (lemma, NER, POS and co-ref).

## Create an Automation Project ##
After clicking on "Create Project" on the Project Page, select "automation" as your project type. The detailled description may be found in the [Project chapter](https://code.google.com/p/webanno/wiki/Projects).

The documents, that are to be annotated, have to be uploaded in the frame "Documents". Please make sure that the chosen format corresponds to the format of the files you are uploading.

<img src='https://webanno.googlecode.com/svn/wiki/images/automation1.JPG' width='600' />

To manage the automation process, choose the Automation frame. The following frame will appear:

<img src='https://webanno.googlecode.com/svn/wiki/images/auto2.PNG' width='600' />

First choose your target layer in the "Select automation layer" frame.
If you want to train a non-custom layer, please make sure you created or imported it in the Layer frame (for instructions to do so, see [New Layer Creation](https://code.google.com/p/webanno/wiki/Projects#New_Layer_Creation)).

<a href='Hidden comment: 
The following frame will appear in the right frame:
<img src="https://webanno.googlecode.com/svn/wiki/images/auto3.PNG" width="600"/>

It gives you the possibility to decide, whether annotations chosen later on on the Automation page will be automatically changed to this annotation or not. After choosing this feature, the following frame will be displayed:
'></a>

<img src='https://webanno.googlecode.com/svn/wiki/images/auto4.PNG' width='600' />

Here you may choose the format of the target layer and optionally add some feature layers on which you want to train.

In the tab "Target layer" you may upload training files containing the target layer in WebAnno Export formats (WebAnno CPH TEI reader, plain text, binary format, XMI format, old WebAnno Format, WebAnno Format, Weblicht TCF Format, for more information on these formats, see [Format](https://code.google.com/p/webanno/wiki/Format?ts=1407336468&updated=Format)).

In the next tab "TAB-SEP target", you may upload training files containing the target layer in a tab-separated format, which is structured by writing each single word in a line together with its target tag, separated by a tab. Sentences are separated by blank lines.

An example of such a file is presented below.
<a href='Hidden comment: 
Put EXAMPLE here
'></a>

The same goes analogically for the feature layers. The "Other layers" tab gives the possiblity to upload WebAnno Export formats and choosing the layers that are to be used in training in the format window. The "TAB-SEP feature" tab gives the possiblity to upload files in the above described tab-separated format, containing the feature tags in the second column. Every file will be regarded as one separate feature.

After choosing the training files, uploading them in the right format and importing them (by clicking on "Import"), every file will be displayed in the corresponding tab in the frame "Documents". Click on the button "Start Automation" on the left, when you have uploaded your training data. Be prepared to wait for some time, as automation is a non-trivial process.

You can see that the automation has finished either by the fact that the  "Start Automation" button is enabled again, or on the Monitoring page, by choosing the project in [Monitoring](https://code.google.com/p/webanno/wiki/Monitoring) and looking at the progress shown in the "Training results /status" frame.

## View and Select Automation ##
To see the tags that were automatically created during the previously described, go to "Home" and choose the "Automation" page.
Then select a project and a file, analogically to Annotation. The page, which is demonstrated below will be displayed.
The navigation, export and the marking of finished documents is the same as in [Annotation](https://code.google.com/p/webanno/wiki/Annotation).

<img src='https://webanno.googlecode.com/svn/wiki/images/auto6.PNG' width='600' />

In the lower part, you see two horizontal frames, the lower one showing the automatatically created annotation.
By clicking on the tags, they are selected and therefore appear in the upper frame "Annotation". You may see that
selected tags turn grey in the "Automation" frame and blue in the "Annotation" frame. You may also add new tags to the "Annotation",
just like on the Annotation page.