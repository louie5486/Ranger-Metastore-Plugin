/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ranger.services.hive;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

public class kerberos_test {
    public static void main(String[] args) throws IOException {

    Configuration conf = new Configuration();
    //The following property is enough for a non-kerberized setup
    //      conf.set("fs.defaultFS", "localhost:9000");

    //need following set of properties to access a kerberized cluster
    conf.set("fs.defaultFS", "hdfs://odp1:9000");
    conf.set("hadoop.security.authentication", "kerberos");

    //The location of krb5.conf file needs to be provided in the VM arguments for the JVM
    //-Djava.security.krb5.conf=/Users/user/Desktop/utils/cluster/dev/krb5.conf

    UserGroupInformation.setConfiguration(conf);
    UserGroupInformation.loginUserFromKeytab("louie@KDC.COM",
            "/Users/louie/hadoop/louie.keytab");
            System.out.println(UserGroupInformation.getCurrentUser().getUserName());
            
            System.out.println(UserGroupInformation.getCurrentUser().getShortUserName());
            for (String str : UserGroupInformation.getCurrentUser().getGroupNames()) {
                System.out.println(str);
            }
    try (FileSystem fs = FileSystem.get(conf);) {
        FileStatus[] fileStatuses = fs.listStatus(new Path("/tmp/workers"));
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println(fileStatus.getPath().getName());
        }
        
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
}
