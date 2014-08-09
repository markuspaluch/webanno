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
package de.tudarmstadt.ukp.clarin.webanno.api.dao;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * A utility class for {@link RepositoryServiceDbData} and {@link AnnotationServiceImpl} classes
 *
 * @author Seid Muhie Yimam
 */
public class DaoUtils
{

    /**
     * While exporting annotation documents, some of the writers generate multiple outputs, e.g. a
     * type system file in addition to the annotation data. This method generates a zip file if the
     * exported file do contain multiple file output
     */
    public static void zipFolder(File srcFolder, File destZipFile)
        throws IOException
    {
        ZipOutputStream zip = null;
        try {
            zip = new ZipOutputStream(new FileOutputStream(destZipFile));

            for (File file : srcFolder.getAbsoluteFile().listFiles()) {
                addToZip(zip, srcFolder.getAbsoluteFile(), file);
            }
            zip.flush();
        }
        finally {
            closeQuietly(zip);
        }
    }

    private static void addToZip(ZipOutputStream zip, File aBasePath, File aPath)
        throws IOException
    {
        if (aPath.isDirectory()) {
            for (File file : aPath.listFiles()) {
                addToZip(zip, aBasePath, file);
            }
        }
        else {
            FileInputStream in = null;
            try {
                in = new FileInputStream(aPath);
                String relativePath = aBasePath.toURI().relativize(aPath.toURI()).getPath();
                zip.putNextEntry(new ZipEntry(relativePath));
                IOUtils.copy(in, zip);
            }
            finally {
                closeQuietly(in);
            }
        }
    }
}
