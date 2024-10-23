package snippet.model.entities

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapKeyColumn
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Entity
class SnippetTest (
    // id de test
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    // id de snippet con el que se relaciona
    @ManyToOne
    @JoinColumn(name = "snippet_id")
    var snippet: Snippet,

    // inputs y sus tipos
    @ElementCollection
    @CollectionTable(name = "snippet_test_inputs", joinColumns = [JoinColumn(name = "snippet_test_id")])
    @MapKeyColumn(name = "input_value")
    @Column(name = "input_type")
    var inputsAndTypes: Map<String, String>,

    // Output único con su tipo
    var outputValue: String,
    var outputType: String
)