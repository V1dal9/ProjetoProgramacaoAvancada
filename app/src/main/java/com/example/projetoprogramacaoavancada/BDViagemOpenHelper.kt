package com.example.projetoprogramacaoavancada

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BDViagemOpenHelper(context: Context?) : SQLiteOpenHelper(context, NOME, null, VERSAO) {
    override fun onCreate(db: SQLiteDatabase?) {
        requireNotNull(db)
        Tabela_Destino(db).criar()
        Tabela_Origem(db).criar()
        Tabela_Lista_Viagem(db).criar()
        Tabela_Companhia_Viagem(db).criar()
        Tabela_Passageiro(db).criar()
        Tabela_Info_Viagem(db).criar()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    companion object{
        const val NOME = "viagem.db"
        private const val VERSAO = 1
    }
}