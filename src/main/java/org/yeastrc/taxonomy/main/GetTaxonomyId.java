package org.yeastrc.taxonomy.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yeastrc.taxonomy.exceptions.GetTaxonomyIdNumberFormatException;


/**
 * Get Taxonomy Id for name and description
 *
 */
public class GetTaxonomyId {

	/**
	 * private contructor
	 */
	private GetTaxonomyId() {}

	
	public static GetTaxonomyId getInstance( ) {
		
		return new GetTaxonomyId();
	}
	


	private Pattern[]  taxIdPatterns = {
			
			//  Make case insensitive  Pattern.CASE_INSENSITIVE
			
			//  Tax_Id=
			
			// MiddleOfLine
			Pattern.compile( "^.*\\s+Tax_Id=(\\d+)\\s+.*$", Pattern.CASE_INSENSITIVE ),
			// StartOfLine = 
			Pattern.compile( "^Tax_Id=(\\d+)\\s+.*$", Pattern.CASE_INSENSITIVE ),
			// EndOfLine = 
			Pattern.compile( "^.*\\s+Tax_Id=(\\d+)$", Pattern.CASE_INSENSITIVE ),
			// AllOfLine = 
			Pattern.compile( "^Tax_Id=(\\d+)$", Pattern.CASE_INSENSITIVE ),

			//  Taxonomy_Id=
			
			// MiddleOfLine = 
			Pattern.compile( "^.*\\s+Taxonomy_Id=(\\d+)\\s+.*$", Pattern.CASE_INSENSITIVE ),
			// StartOfLine = 
			Pattern.compile( "^Taxonomy_Id=(\\d+)\\s+.*$", Pattern.CASE_INSENSITIVE ),
			// EndOfLine = 
			Pattern.compile( "^.*\\s+Taxonomy_Id=(\\d+)$", Pattern.CASE_INSENSITIVE ),
			// AllOfLine = 
			Pattern.compile( "^Taxonomy_Id=(\\d+)$", Pattern.CASE_INSENSITIVE )
	};

	

	/**
	 * @param name
	 * @param description
	 * @return taxonomy id, or null if cannot determine it 
	 */
	public Integer getTaxonomyId( String name, String description ) throws GetTaxonomyIdNumberFormatException {

		
		Integer taxonomyId = null;
		
		if ( description == null ) {
			
			
			
		} else {
			
			for ( int index = 0; index < taxIdPatterns.length; index++ ) {
				
				Pattern taxIdPattern = taxIdPatterns[ index ];

				Matcher gm = null;

				// Check to see if they have a Tax_Id=# as part of their desc.  If so, use that #
				gm = taxIdPattern.matcher( description );
				if (gm.matches()) {

					String taxIdString = gm.group( 1 );

					try {

						taxonomyId = Integer.parseInt( taxIdString );

					} catch ( NumberFormatException e ) {

						//  Never get this exception since the regex pattern only applies to digits after the "="

						String taxIdLabel = "Tax_Id";
						
						if ( index > 3 ) {
							
							taxIdLabel = "Taxonomy_Id";
						}
						
						String msg = "FASTA header that contains '" + taxIdLabel 
								+ "=' does not have an integer for the value."
								+ "  " + taxIdLabel + " value  (delim '|'): |" + taxIdString + "|.";

						throw new GetTaxonomyIdNumberFormatException( msg );
					}

					break;  //  EARLY EXIT loop
				}
			}
			
			
		}
		
		return taxonomyId;
		
	}
}
