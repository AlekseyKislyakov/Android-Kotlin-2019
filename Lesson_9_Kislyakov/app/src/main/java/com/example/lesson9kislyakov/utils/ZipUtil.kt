package com.example.lesson9kislyakov.utils

import android.util.Log
import androidx.annotation.WorkerThread
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class Decompress(private val _zipFile: String, private val _location: String) {

    init {
        dirChecker("")
    }

    @WorkerThread
    fun unzip(): String {
        try {
            val fin = FileInputStream(_zipFile)
            val zin = ZipInputStream(fin)
            var ze: ZipEntry? = zin.nextEntry
            while (ze != null) {
                Log.d("myTag", "Unzipping " + ze.name)

                if (ze.isDirectory) {
                    dirChecker(ze.name)
                } else {
                    val f = File(_location + ze.name)
                    f.delete()
                    val fout = FileOutputStream(_location + ze.name)
                    var c = zin.read()
                    while (c != -1) {
                        fout.write(c)
                        c = zin.read()
                    }

                    zin.closeEntry()
                    fout.close()
                    return _location + ze.name
                }
                ze = zin.nextEntry
            }
            zin.close()
        } catch (e: Exception) {
            Log.e("Decompress", "unzip", e)
        }

        return "_location + ze.getName()"
    }

    private fun dirChecker(dir: String) {
        val f = File(_location + dir)

        if (!f.isDirectory) {
            f.mkdirs()
        }
    }
}