package org.xwiki.platform;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xwiki.platform.patchservice.api.Operation;
import org.xwiki.platform.patchservice.api.RWOperation;
import org.xwiki.platform.patchservice.impl.OperationFactoryImpl;

import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;

public class ContentOperationsTest extends TestCase
{
    Document domDoc;

    XWikiDocument doc;

    protected void setUp()
    {
        try {
            domDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            doc = new XWikiDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void testApplyContentInsertOperation() throws XWikiException
    {
        doc.setContent("this is the content");
        RWOperation operation =
            OperationFactoryImpl.getInstance().newOperation(RWOperation.TYPE_CONTENT_INSERT);
        operation.insert("new ", 12);
        operation.apply(doc);
        assertEquals("this is the new content", doc.getContent());
    }

    public void testXmlRoundtripContentInsertOperation() throws XWikiException
    {
        RWOperation operation =
            OperationFactoryImpl.getInstance().newOperation(RWOperation.TYPE_CONTENT_INSERT);
        operation.insert("added <con\"ten>t", 10);
        Element e = operation.toXml(domDoc);
        Operation loadedOperation = OperationFactoryImpl.getInstance().loadOperation(e);
        assertEquals(loadedOperation, operation);
    }

    public void testInvalidContentInsertOperation() throws XWikiException
    {
        doc.setContent("this is the short content");
        RWOperation operation =
            OperationFactoryImpl.getInstance().newOperation(RWOperation.TYPE_CONTENT_INSERT);
        operation.insert("something", 42);
        try {
            operation.apply(doc);
            assertFalse(true);
        } catch (XWikiException ex) {
            // This is expected
        }
    }

    public void testApplyContentDeleteOperation() throws XWikiException
    {
        doc.setContent("this is the old content");
        RWOperation operation =
            OperationFactoryImpl.getInstance().newOperation(RWOperation.TYPE_CONTENT_DELETE);
        operation.delete("old ", 12);
        operation.apply(doc);
        assertEquals("this is the content", doc.getContent());
    }

    public void testXmlRoundtripContentDeleteOperation() throws XWikiException
    {
        RWOperation operation =
            OperationFactoryImpl.getInstance().newOperation(RWOperation.TYPE_CONTENT_DELETE);
        operation.delete("something", 10);
        Element e = operation.toXml(domDoc);
        Operation loadedOperation = OperationFactoryImpl.getInstance().loadOperation(e);
        assertEquals(operation, loadedOperation);
    }

    public void testInvalidContentDeleteOperation() throws XWikiException
    {
        doc.setContent("this is the short content");
        RWOperation operation =
            OperationFactoryImpl.getInstance().newOperation(RWOperation.TYPE_CONTENT_DELETE);
        operation.delete("something", 42);
        try {
            operation.apply(doc);
            assertFalse(true);
        } catch (XWikiException ex) {
            // This is expected
        }
        operation.delete("this", 2);
        try {
            operation.apply(doc);
            assertFalse(true);
        } catch (XWikiException ex) {
            // This is expected
        }
    }

    public void testConsecutiveContentInsertDeleteOperations() throws XWikiException
    {
        doc.setContent("this is the old content");
        RWOperation operation =
            OperationFactoryImpl.getInstance().newOperation(RWOperation.TYPE_CONTENT_DELETE);
        operation.delete("old", 12);
        operation.apply(doc);
        operation =
            OperationFactoryImpl.getInstance().newOperation(RWOperation.TYPE_CONTENT_INSERT);
        operation.insert("new", 12);
        operation.apply(doc);
        assertEquals("this is the new content", doc.getContent());
        operation =
            OperationFactoryImpl.getInstance().newOperation(RWOperation.TYPE_CONTENT_INSERT);
        operation.insert("restored", 15);
        operation.apply(doc);
        operation =
            OperationFactoryImpl.getInstance().newOperation(RWOperation.TYPE_CONTENT_DELETE);
        operation.delete("new", 12);
        operation.apply(doc);
        assertEquals("this is the restored content", doc.getContent());
    }
}
