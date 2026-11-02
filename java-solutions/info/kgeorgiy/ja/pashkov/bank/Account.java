package info.kgeorgiy.ja.pashkov.bank;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Account extends Remote, Serializable {
    String getId() throws RemoteException;

    long getAmount() throws RemoteException;

    void setAmount(long amount) throws RemoteException;
}

//NOTE -- CE

// Committing __current-repo
//On branch main
//Your branch is up to date with 'origin/main'.
//
//nothing to commit, working tree clean
//Pushing __current-repo
//Everything up-to-date
//Pulling __local/git/Pashkov_Dmitrii
//Already up to date.
//Removing __current-repo
//Copying __local/git/Pashkov_Dmitrii to __current-repo
//commit cfede039805397b8000b2a0ac57f3acad133feed
//Author: erzherzog <enferson.new@gmail.com>
//Date:   Wed May 27 12:50:12 2026 +0300
//
//    Checked by an instructor
//Compiling 15 Java sources
//LinuxDockerSandbox[NATIVE]: Exec compile
/// mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankTests.java:3: error: package org.junit.platform.engine.discovery is not visible
//import org.junit.platform.engine.discovery.DiscoverySelectors;
//                                ^
//  (package org.junit.platform.engine.discovery is declared in module org.junit.platform.engine, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankTests.java:4: error: package org.junit.platform.launcher is not visible
//import org.junit.platform.launcher.Launcher;
//                         ^
//  (package org.junit.platform.launcher is declared in module org.junit.platform.launcher, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankTests.java:5: error: package org.junit.platform.launcher is not visible
//import org.junit.platform.launcher.LauncherDiscoveryRequest;
//                         ^
//  (package org.junit.platform.launcher is declared in module org.junit.platform.launcher, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankTests.java:6: error: package org.junit.platform.launcher.core is not visible
//import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
//                                  ^
//  (package org.junit.platform.launcher.core is declared in module org.junit.platform.launcher, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankTests.java:7: error: package org.junit.platform.launcher.core is not visible
//import org.junit.platform.launcher.core.LauncherFactory;
//                                  ^
//  (package org.junit.platform.launcher.core is declared in module org.junit.platform.launcher, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankTests.java:8: error: package org.junit.platform.launcher.listeners is not visible
//import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
//                                  ^
//  (package org.junit.platform.launcher.listeners is declared in module org.junit.platform.launcher, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankTests.java:9: error: package org.junit.platform.launcher.listeners is not visible
//import org.junit.platform.launcher.listeners.TestExecutionSummary;
//                                  ^
//  (package org.junit.platform.launcher.listeners is declared in module org.junit.platform.launcher, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankJUnitRunner.java:3: error: package org.junit.platform.engine.discovery is not visible
//import org.junit.platform.engine.discovery.DiscoverySelectors;
//                                ^
//  (package org.junit.platform.engine.discovery is declared in module org.junit.platform.engine, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankJUnitRunner.java:4: error: package org.junit.platform.launcher is not visible
//import org.junit.platform.launcher.Launcher;
//                         ^
//  (package org.junit.platform.launcher is declared in module org.junit.platform.launcher, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankJUnitRunner.java:5: error: package org.junit.platform.launcher is not visible
//import org.junit.platform.launcher.LauncherDiscoveryRequest;
//                         ^
//  (package org.junit.platform.launcher is declared in module org.junit.platform.launcher, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankJUnitRunner.java:6: error: package org.junit.platform.launcher.core is not visible
//import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
//                                  ^
//  (package org.junit.platform.launcher.core is declared in module org.junit.platform.launcher, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankJUnitRunner.java:7: error: package org.junit.platform.launcher.core is not visible
//import org.junit.platform.launcher.core.LauncherFactory;
//                                  ^
//  (package org.junit.platform.launcher.core is declared in module org.junit.platform.launcher, which is not in the module graph)
///mnt/repo/java-solutions/info/kgeorgiy/ja/pashkov/bank/BankJUnitRunner.java:8: error: package org.junit.platform.launcher.listeners is not visible
//import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
//                                  ^
//  (package org.junit.platform.launcher.listeners is declared in module org.junit.platform.launcher, which is not in the module graph)
//warning: unknown enum constant Status.STABLE
//  reason: class file for org.apiguardian.api.API$Status not found
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.EXPERIMENTAL
//warning: unknown enum constant Status.STABLE
//  reason: class file for org.apiguardian.api.API$Status not found
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.DEPRECATED
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.STABLE
//warning: unknown enum constant Status.MAINTAINED
//13 errors
//31 warnings
//ERROR: Compilation failed