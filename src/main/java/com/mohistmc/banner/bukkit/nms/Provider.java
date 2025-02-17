package com.mohistmc.banner.bukkit.nms;

import java.io.File;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public interface Provider {

    public boolean remap(File jarFile);

    public boolean runSpecialSource(File mappingsFile, File inJar, File outJar);

    public boolean shouldReplaceASM();

    public MethodVisitor newMethodVisitor(int api, MethodVisitor visitMethod, String aname);

    public ClassVisitor getClassVisitor(int api, ClassVisitor classVisitor);

}
