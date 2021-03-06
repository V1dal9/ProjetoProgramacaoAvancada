package com.example.projetoprogramacaoavancada

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.provider.BaseColumns


class TabelaListaViagem (db:SQLiteDatabase):TabelaBD(db, NOME) {
    override fun cria() {
        db.execSQL("CREATE TABLE $nome(${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$CAMPO_NOME TEXT, " +
                "$ROUPA TEXT, " +
                "$CALCADO TEXT, " +
                "$ACESSORIO TEXT, " +
                "$ELETRONICO TEXT, " +
                "$HIGIENE TEXT, " +
                "$PASSAGEIRO_ID INTEGER NOT NULL, " +
                "$INFOVIAGEM_ID INTEGER NOT NULL, " +
                "FOREIGN KEY($PASSAGEIRO_ID) " +
                "REFERENCES ${Tabela_Passageiro.NOME}(${BaseColumns._ID}) ON DELETE RESTRICT," +
                "FOREIGN KEY($INFOVIAGEM_ID) " +
                "REFERENCES ${Tabela_Info_Viagem_Bilhete.NOME}(${BaseColumns._ID}) ON DELETE RESTRICT)")
    }

    override fun query(
        columns: Array<String>,
        selection: String?,
        selectionArgs: Array<String>?,
        groupBy: String?,
        having: String?,
        orderBy: String?
    ): Cursor {
        val queryBuilderPassageiros = SQLiteQueryBuilder()
        queryBuilderPassageiros.tables = "$NOME INNER JOIN ${Tabela_Passageiro.NOME} ON ${Tabela_Passageiro.NOME}.${BaseColumns._ID} = $PASSAGEIRO_ID INNER JOIN ${Tabela_Info_Viagem_Bilhete.NOME} ON ${Tabela_Info_Viagem_Bilhete.NOME}.${BaseColumns._ID} = $INFOVIAGEM_ID"

        return queryBuilderPassageiros.query(db, columns, selection, selectionArgs, groupBy, having, orderBy)

    }

    companion object{
        const val NOME = "ListaViagem"

        const val ID_LISTA = "$NOME.${BaseColumns._ID}"
        const val CAMPO_NOME = "nomeListaViagem"
        const val ROUPA = "roupas"
        const val CALCADO = "calcados"
        const val ACESSORIO = "accessories"
        const val ELETRONICO = "acessoriosInformaticos"
        const val HIGIENE = "produtosHigienicos"
        const val PASSAGEIRO_ID  = "passageiroID"
        const val INFOVIAGEM_ID = "infoviagemID"

        val TODAS_COLUNAS = arrayOf(
            ID_LISTA,
            CAMPO_NOME,
            ROUPA,
            CALCADO,
            ACESSORIO,
            ELETRONICO,
            HIGIENE,
            PASSAGEIRO_ID,
            INFOVIAGEM_ID,
            Tabela_Passageiro.CAMPO_NOME,
            Tabela_Passageiro.GENERO,
            Tabela_Passageiro.IDADE,
            Tabela_Passageiro.CAMPO_ID,
            Tabela_Info_Viagem_Bilhete.CLASS_VIAGEM,
            Tabela_Info_Viagem_Bilhete.TIPO_MALA,
            Tabela_Info_Viagem_Bilhete.DATA_FIM,
            Tabela_Info_Viagem_Bilhete.DATA_INICIO,
            Tabela_Info_Viagem_Bilhete.LOCAL_ORIGEM,
            Tabela_Info_Viagem_Bilhete.LOCAL_DESTINO,
            Tabela_Info_Viagem_Bilhete.CAMPO_ID)
    }

}