/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */
package com.microsoft.azure.eventhubs.lib;

import java.time.Duration;

import com.microsoft.azure.eventhubs.*;
import com.microsoft.azure.eventhubs.impl.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class SasTokenTestBase extends ApiTestBase {
    
    private static ConnectionStringBuilder originalConnectionString;
    
    @BeforeClass
    public static void replaceConnectionString()  throws Exception {
        
        originalConnectionString = TestContext.getConnectionString();
        final String connectionStringWithSasToken = new ConnectionStringBuilder()
                .setEndpoint(originalConnectionString.getEndpoint())
                .setEventHubName(originalConnectionString.getEventHubName())
                .setSharedAccessSignature(
                    SharedAccessSignatureTokenProvider.generateSharedAccessSignature(originalConnectionString.getSasKeyName(),
                            originalConnectionString.getSasKey(), 
                            String.format("amqp://%s/%s", originalConnectionString.getEndpoint().getHost(), originalConnectionString.getEventHubName()),
                            Duration.ofDays(1))
                )
                .toString();

        TestContext.setConnectionString(connectionStringWithSasToken);
    }
    
    @AfterClass
    public static void undoReplace() throws EventHubException {

        if (originalConnectionString != null)
            TestContext.setConnectionString(originalConnectionString.toString());
    }
}
