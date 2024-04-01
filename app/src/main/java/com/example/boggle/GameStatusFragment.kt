import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.boggle.R

public class GameStatusFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newButton : android.widget.Button = view.findViewById(R.id.btnNewGame)
        var score = view.findViewById<TextView>(R.id.score_num)

        // Set click listeners
        newButton.setOnClickListener { resetGame() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gamestatusfragment, container, false)
        return view
    }

    private fun resetGame() {
        // Get the score TextView and set its text to "0"
        val scoreTextView = requireView().findViewById<TextView>(R.id.score_num)
        scoreTextView.text = "0"

        // If you need to reset any other game state, you can add the necessary code here
    }
}
