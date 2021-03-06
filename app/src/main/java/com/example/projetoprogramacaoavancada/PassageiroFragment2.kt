package com.example.projetoprogramacaoavancada

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetoprogramacaoavancada.databinding.FragmentPassageiro2Binding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
class PassageiroFragment2 : Fragment(), LoaderManager.LoaderCallbacks<Cursor>{
    var passageiroSelecionado : Passageiro? = null
        get() = field
        set(value){
            field = value
            (requireActivity() as MainActivity).mostraOpcaoAlterarEliminar(field != null)
        }

    private var _binding: FragmentPassageiro2Binding? = null
    private var adapterPassageiro : AdapterPassageiro? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPassageiro2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        LoaderManager.getInstance(this).initLoader(ID_LOADER_PASSAGEIRO, null, this)

        adapterPassageiro = AdapterPassageiro(this)
        binding.recycleViewPassageiro.adapter = adapterPassageiro
        binding.recycleViewPassageiro.layoutManager = LinearLayoutManager(requireContext())

        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_lista
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
        CursorLoader(
            requireContext(),
            ContentProviderPassageiro.ENDERECO_PASSAGEIRO,
            Tabela_Passageiro.TODAS_COLUNAS,
            null,
            null,
            Tabela_Passageiro.CAMPO_NOME
        )


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapterPassageiro!!.cursor = data
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        if(_binding == null) return
        adapterPassageiro!!.cursor = null
    }

    fun processaOpcaoMenu(item: MenuItem) : Boolean =
        when(item.itemId) {
            R.id.action_inserir -> {
                val acao = PassageiroFragment2Directions.actionPassageiroFragment2ToEditarPassageirosFragment()
                findNavController().navigate(acao)
                (activity as MainActivity).atualizaNome("Inserir Passageiro")
                true
            }
            R.id.action_edit -> {
                val acao = PassageiroFragment2Directions.actionPassageiroFragment2ToEditarPassageirosFragment()
                findNavController().navigate(acao)
                (activity as MainActivity).atualizaNome("Alterar Passageiro")
                true
            }
            R.id.action_eliminar -> {
                //val acao = PassageiroFragment2Directions
                //findNavController().navigate(acao)
                true
            }
            else -> false
        }

    companion object{
        const val ID_LOADER_PASSAGEIRO = 0
    }
}