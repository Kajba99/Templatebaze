package com.example.templatebaze

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.templatebaze.dao.BazaDAO
import com.example.templatebaze.dao.DAO
import com.example.templatebaze.model.Student
import com.example.templatebaze.model.StudentAdapter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity( ) {

    lateinit var tvId    : TextView
    lateinit var etIme   : EditText
    lateinit var etDatum : EditText
    lateinit var etGlazba : EditText
    lateinit var sGodina : Spinner
    lateinit var rgSpol  : RadioGroup
    lateinit var rbM     : RadioButton
    lateinit var rbZ     : RadioButton
    private lateinit var rvLista : RecyclerView

    private lateinit var dao     : DAO
    private lateinit var adapter : StudentAdapter


    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_main )

        // dao = InMemory( )
        dao = BazaDAO( this )

        initWidgets( )


    }




    fun initWidgets( ) {
        tvId    = findViewById( R.id.tvId )
        etIme   = findViewById( R.id.etIme )
        etGlazba= findViewById( R.id.etGlazba )
        etDatum = findViewById( R.id.etDatum )
        sGodina = findViewById( R.id.sGodina )
        rgSpol  = findViewById( R.id.rgSpol )
        rbM     = findViewById( R.id.rbM )
        rbZ     = findViewById( R.id.rbZ )

        rvLista = findViewById( R.id.rvListaStudenata )

        adapter = StudentAdapter( this, dao.getAll( ) )
        rvLista.layoutManager = LinearLayoutManager( this )
        rvLista.adapter = adapter

    }

    fun odabirdatuma( v : View ) {
        val dan = Calendar.getInstance( )
        try {
            dan.time = SimpleDateFormat( "dd.MM.yyyy." ).parse( etDatum.text.toString( ) )
        } catch ( e : Exception ) { }
        DatePickerDialog(
            this,
            {
                    dp, g, m, d ->
                val mm = m+1      // Mjeseci su numerirani od 0 do 11 (ne od 1 do 12)
                etDatum.setText(
                    ( if( d<10 ) "0$d" else "$d" ) + "." +
                            ( if( mm<10 ) "0$mm" else "$mm" ) + "." +
                            "$g."
                )
            },
            dan.get( Calendar.YEAR ),
            dan.get( Calendar.MONTH ),
            dan.get( Calendar.DAY_OF_MONTH )
        ).show( )
    }

    fun insert( v : View ) {
        if( dao.insert( ucitaj( ) ) )
            osvjeziListu( "Novi student je spemljen!" )
    }

    fun update( v : View ) {
        if( dao.update( ucitaj( ) ) )
            osvjeziListu( "Podaci o studentu su promijenjeni!" )
    }

    fun delete( v : View ) {


        if( dao.delete( ucitaj( ) ) )
            osvjeziListu( "Podaci o studentu su promijenjeni!" )
    }

    fun osvjeziListu( poruka : String? ) {
        if( poruka != null )
            Toast
                .makeText( this, poruka, Toast.LENGTH_SHORT )
                .show( )
        adapter         = StudentAdapter( this, dao.getAll( ) )
        rvLista.adapter = adapter
        ocistiFormu( )
    }

    fun ucitaj( ) : Student {
        val sdfCitanje = SimpleDateFormat( "dd.MM.yyyy." )
        val sdfInt     = SimpleDateFormat( "yyyyMMdd" )
        val id : Long? = if( tvId.text=="" ) null else tvId.text.toString( ).toLong( )
        val ime = etIme.text.toString( )
        val glazba = etGlazba.text.toString()
        val rodendan = sdfInt.format( sdfCitanje.parse( etDatum.text.toString( ) ) ).toInt( )
        val godina = sGodina.selectedItemPosition+1
        val spol   = if( rbM.isChecked ) "M"
        else if( rbZ.isChecked ) "Ž"
        else ""

        return Student( id, ime, rodendan, godina, spol , glazba)
    }

    fun ocistiFormu( ) {
        tvId.setText( "" )
        etIme.setText( "" )
        etDatum.setText( "" )
        etGlazba.setText( "" )
        sGodina.setSelection( 0 )
        rgSpol.clearCheck( )
    }
}