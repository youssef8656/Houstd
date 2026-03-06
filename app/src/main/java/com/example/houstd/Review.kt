import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.houstd.R

class ReviewItemFragment : Fragment(R.layout.fragment_review_item) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = requireArguments().getString("name").orEmpty()
        val faculty = requireArguments().getString("faculty").orEmpty()
        val rating = requireArguments().getFloat("rating", 0f)
        val date = requireArguments().getString("date").orEmpty()
        val comment = requireArguments().getString("comment").orEmpty()
        val meta = requireArguments().getString("meta").orEmpty()

        view.findViewById<TextView>(R.id.tvName).text = name
        view.findViewById<TextView>(R.id.tvFaculty).text = faculty
        view.findViewById<RatingBar>(R.id.ratingBar).rating = rating
        view.findViewById<TextView>(R.id.tvRatingValue).text = "${rating}/5"
        view.findViewById<TextView>(R.id.tvDate).text = date
        view.findViewById<TextView>(R.id.tvComment).text = comment
        view.findViewById<TextView>(R.id.tvMeta).text = meta
    }

    companion object {
        fun newInstance(
            name: String,
            faculty: String,
            rating: Float,
            date: String,
            comment: String,
            meta: String
        ) = ReviewItemFragment().apply {
            arguments = Bundle().apply {
                putString("name", name)
                putString("faculty", faculty)
                putFloat("rating", rating)
                putString("date", date)
                putString("comment", comment)
                putString("meta", meta)
            }
        }
    }
}