package com.example.projetoprogramacaoavancada

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class ContentProviderBilhete : ContentProvider() {

    var dbOpenHelper : BDViagemOpenHelper? = null

    override fun onCreate(): Boolean {
        dbOpenHelper = BDViagemOpenHelper(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbOpenHelper!!.readableDatabase

        requireNotNull(projection)

        val colunas = projection as Array<String>
        val argsSelecao = selectionArgs as Array<String>?
        val id = uri.lastPathSegment

        val cursor = when (ContentProviderViagem.getUriMatcher().match(uri)){
            URI_PASSAGEIRO -> Tabela_Passageiro(db).query(colunas, selection, argsSelecao, null, null, sortOrder)
            URI_PASSAGEIRO_ESPECIFICO -> Tabela_Passageiro(db).query(colunas, "${BaseColumns._ID}=?", arrayOf("${id}"), null,null, null)
            URI_COMPANHIA_VIAGEM -> Tabela_Companhia_Viagem(db).query(colunas, selection, argsSelecao, null, null, sortOrder)
            URI_COMPANHIA_ESPECIFICA -> Tabela_Companhia_Viagem(db).query(colunas, "${BaseColumns._ID}=?", arrayOf("${id}"), null,null, null)
            URI_BILHETE_VIAGEM -> Tabela_Info_Viagem_Bilhete(db).query(colunas, selection, argsSelecao, null, null, sortOrder)
            URI_BILHETE_ESPECIFICA -> Tabela_Info_Viagem_Bilhete(db).query(colunas, "${BaseColumns._ID}=?", arrayOf("${id}"), null,null, null)
            URI_LOCAL_VIAGEM -> Tabela_Info_Viagem_Bilhete(db).query(colunas, selection, argsSelecao, null, null, sortOrder)
            URI_LOCAL_ESPECIFICA -> TabelaLocal(db).query(colunas, "${BaseColumns._ID}=?", arrayOf("${id}"), null,null, null)
            else -> null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? =
        when (ContentProviderViagem.getUriMatcher().match(uri)) {
            URI_PASSAGEIRO -> "${MULTIPLOS_REGISTOS}/${Tabela_Passageiro.NOME}"
            URI_PASSAGEIRO_ESPECIFICO -> "${UNICO_REGISTO}/${Tabela_Passageiro.NOME}"
            URI_COMPANHIA_ESPECIFICA -> "${UNICO_REGISTO}/${Tabela_Companhia_Viagem.NOME}"
            URI_COMPANHIA_VIAGEM -> "${MULTIPLOS_REGISTOS}/${Tabela_Companhia_Viagem.NOME}"
            URI_BILHETE_VIAGEM -> "${MULTIPLOS_REGISTOS}/${Tabela_Info_Viagem_Bilhete.NOME}"
            URI_BILHETE_ESPECIFICA -> "${UNICO_REGISTO}/${Tabela_Info_Viagem_Bilhete.NOME}"
            URI_LOCAL_VIAGEM -> "${MULTIPLOS_REGISTOS}/${Tabela_Passageiro.NOME}"
            URI_LOCAL_ESPECIFICA -> "${UNICO_REGISTO}/${TabelaLocal.NOME}"
            else -> null
        }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbOpenHelper!!.writableDatabase

        requireNotNull(values)

        val id = when (ContentProviderViagem.getUriMatcher().match(uri)) {
            URI_PASSAGEIRO -> Tabela_Passageiro(db).insert(values)
            URI_COMPANHIA_VIAGEM -> Tabela_Companhia_Viagem(db).insert(values)
            URI_BILHETE_VIAGEM -> Tabela_Info_Viagem_Bilhete(db).insert(values)
            URI_LOCAL_VIAGEM -> TabelaLocal(db).insert(values)
            else -> -1
        }

        db.close()

        if (id == -1L) return null

        return Uri.withAppendedPath(uri, "$id")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbOpenHelper!!.writableDatabase

        val id = uri.lastPathSegment
        val registosApagados = when (ContentProviderViagem.getUriMatcher().match(uri)) {
            URI_PASSAGEIRO_ESPECIFICO -> Tabela_Passageiro(db).delete("${BaseColumns._ID}=?", arrayOf("${id}"))
            URI_COMPANHIA_ESPECIFICA -> Tabela_Companhia_Viagem(db).delete("${BaseColumns._ID} = ?", arrayOf("${id}"))
            URI_BILHETE_ESPECIFICA -> Tabela_Info_Viagem_Bilhete(db).delete("${BaseColumns._ID} = ?", arrayOf("${id}"))
            URI_LOCAL_ESPECIFICA -> Tabela_Info_Viagem_Bilhete(db).delete("${BaseColumns._ID} = ?", arrayOf("${id}"))
            else -> 0
        }

        db.close()

        return registosApagados
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        requireNotNull(values)

        val db = dbOpenHelper!!.writableDatabase

        val id = uri.lastPathSegment

        val registosAlterados = when (ContentProviderViagem.getUriMatcher().match(uri)) {
            URI_PASSAGEIRO_ESPECIFICO -> Tabela_Passageiro(db).update(values,"${BaseColumns._ID}=?", arrayOf("${id}"))
            URI_BILHETE_ESPECIFICA -> Tabela_Info_Viagem_Bilhete(db).update(values,"${BaseColumns._ID} = ?", arrayOf("${id}"))
            URI_COMPANHIA_ESPECIFICA -> Tabela_Companhia_Viagem(db).update(values,"${BaseColumns._ID} = ?", arrayOf("${id}"))
            URI_LOCAL_ESPECIFICA -> Tabela_Info_Viagem_Bilhete(db).update(values,"${BaseColumns._ID} = ?", arrayOf("${id}"))
            else -> 0
        }
        db.close()

        return registosAlterados
    }

    companion object{
        private const val AUTHORITY = "com.example.projetoprogramacaoavancada"

        const val URI_BILHETE_VIAGEM = 400
        const val URI_BILHETE_ESPECIFICA = 401

        const val URI_PASSAGEIRO = 100
        const val URI_PASSAGEIRO_ESPECIFICO = 101

        const val URI_LOCAL_VIAGEM = 500
        const val URI_LOCAL_ESPECIFICA = 501

        const val URI_COMPANHIA_VIAGEM = 300
        const val URI_COMPANHIA_ESPECIFICA = 301

        const val UNICO_REGISTO = "vnd.android.cursor.item"
        const val MULTIPLOS_REGISTOS = "vnd.android.cursor.dir"

        private val ENDERCO_BASE = Uri.parse("content://${AUTHORITY}")

        val ENDERECO_BILHETE = Uri.withAppendedPath(ENDERCO_BASE, Tabela_Info_Viagem_Bilhete.NOME)
        fun getUriMatcher() : UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            uriMatcher.addURI(AUTHORITY, Tabela_Passageiro.NOME, URI_PASSAGEIRO)
            uriMatcher.addURI(AUTHORITY, "${Tabela_Passageiro.NOME}/#", URI_PASSAGEIRO_ESPECIFICO)

            uriMatcher.addURI(AUTHORITY, Tabela_Info_Viagem_Bilhete.NOME, URI_BILHETE_VIAGEM)
            uriMatcher.addURI(AUTHORITY, "${Tabela_Info_Viagem_Bilhete.NOME}/#", URI_BILHETE_ESPECIFICA)

            uriMatcher.addURI(AUTHORITY, TabelaLocal.NOME, URI_LOCAL_VIAGEM)
            uriMatcher.addURI(AUTHORITY, "${TabelaLocal.NOME}/#", URI_LOCAL_ESPECIFICA)

            uriMatcher.addURI(AUTHORITY, Tabela_Companhia_Viagem.NOME, URI_COMPANHIA_VIAGEM)
            uriMatcher.addURI(AUTHORITY, "${Tabela_Companhia_Viagem.NOME}/#", URI_COMPANHIA_ESPECIFICA)
            return uriMatcher
        }

    }

}